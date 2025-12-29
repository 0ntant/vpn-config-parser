package app.service;


import app.mapper.VpnDataMapper;
import app.model.VpnData;
import app.model.VpnDataError;
import app.util.XrayConfigConverter;
import app.util.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static app.config.CheckStatusConfig.INVALID_MBPS;
import static app.config.DirConfig.XRAY_TEMP_CONF_DIR;

@Slf4j
public class VpnDataService
{
    LinksProvider linkProvider;
    ConnectionCheckService checkService;
    List<VpnData> vpnData ;
    ObjectMapper mapper;

    public VpnDataService(LinksProvider linkProvider)
    {
        this.linkProvider = linkProvider;

        checkService = new ConnectionCheckService();
        vpnData = new ArrayList<>();
        mapper = new ObjectMapper();
    }

    public VpnDataService()
    {
        linkProvider = new LinkProviderOutlinekeysService();
        checkService = new ConnectionCheckService();
        vpnData = new ArrayList<>();
        mapper = new ObjectMapper();
    }

    public List<VpnData> fillSsLinkField (String country)
    {
        List<String> ssLinks = linkProvider.getAllSsLinksByCountry(country);

        for (String ssLink : ssLinks)
        {
            vpnData.add(createVpnConfig(ssLink));
        }

        cleanConfigs();

        vpnData.removeIf(data -> data.getMbps() == INVALID_MBPS);
        vpnData.removeIf(data -> data.getErrorStatus() == VpnDataError.INVALID_CONFIG);

        return vpnData;
    }

    public void asyncWriteInfoFile(String country)
    {
        OutfileService outfileService = new OutfileService();
        int pages = linkProvider.getPageSize(country);

        for(int i = 0 ; i < pages ; i++)
        {
            List<String> links = linkProvider.getPageLinks(country, i);
            log.info("Get {} links", links.size());
            for(String link : links)
            {
                VpnData dataConfig = createVpnConfig(link);
                if (dataConfig.getMbps() != INVALID_MBPS && dataConfig.getErrorStatus() != VpnDataError.INVALID_CONFIG)
                {
                    log.info("Write config={}", dataConfig.getSsLink());
                    outfileService.sendVpnConf(List.of(dataConfig));
                }
            }

        }

        cleanConfigs();
    }

    private VpnData createVpnConfig(String ssLink)
    {
        VpnData vpnConfig = VpnData.builder()
                            .ssLink(ssLink)
                            .errorStatus(VpnDataError.NONE)
                            .build();

        log.info("Start fill {} link", ssLink);

        runStep(vpnConfig, this::fillTempDirField);
        runStep(vpnConfig, this::fillHostPortFields);
        runStep(vpnConfig, this::fillPingMbpsFields);

        return  vpnConfig;
    }

    private void runStep(VpnData vpn, Consumer<VpnData> step)
    {
        if (vpn.getErrorStatus() == VpnDataError.NONE)
        {
            step.accept(vpn);
        }
    }

    public void fillTempDirField(VpnData vpnData)
    {
        try
        {
            String tempDir = createTempConfig(vpnData.getSsLink());
            vpnData.setTempDir(tempDir);
        }
        catch (RuntimeException ex)
        {
            log.warn("ssLink: {} invalid, config skip", vpnData.getSsLink());
            vpnData.setErrorStatus(VpnDataError.INVALID_CONFIG);
        }

    }

    public String createTempConfig(String ssLink)
    {
        String fileName =  String.format("config_%s.json", UUID.randomUUID());
        JsonNode jsonConfig = XrayConfigConverter.linkToJson(ssLink);
        try
        {
            return FileUtil
                    .create(
                            Paths.get(
                                    String.format(
                                            "%s/%s",
                                            XRAY_TEMP_CONF_DIR,
                                            fileName
                                    )
                            ),
                            mapper.writeValueAsString(jsonConfig)
                    );
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public void fillHostPortFields(VpnData vpnData)
    {
        String jsonString = FileUtil.read(vpnData.getTempDir());
        try
        {
            VpnData tempValues = VpnDataMapper.mapHostPort(mapper.readTree(jsonString));

            vpnData.setHost(tempValues.getHost());
            vpnData.setPort(tempValues.getPort());
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public void fillPingMbpsFields(VpnData vpnData)
    {
        checkService.fillFields(vpnData);
    }

    private void cleanConfigs()
    {
        log.info("Clean temp configs");
        FileUtil.getRegularFiles(XRAY_TEMP_CONF_DIR).stream()
                .filter(path -> path.getFileName().toString().startsWith("config_"))
                .forEach(path -> {
                    boolean deleted = FileUtil.delete(path.toString());
                    if (!deleted) {
                        log.warn("File not found or not deleted: {}", path);
                    }
                });
    }
}
