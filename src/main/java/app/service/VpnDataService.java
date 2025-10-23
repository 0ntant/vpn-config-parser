package app.service;


import app.mapper.VpnDataMapper;
import app.model.VpnData;
import app.util.XrayConfigConverter;
import app.util.XrayConfigFileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static app.config.CheckStatusConfig.INVALID_MBPS;

@Slf4j
public class VpnDataService
{
    OutlinekeysService outlinekeysService;
    ConnectionCheckService checkService;
    List<VpnData> vpnData ;
    ObjectMapper mapper;

    public VpnDataService()
    {
        outlinekeysService = new OutlinekeysService();
        checkService = new ConnectionCheckService();
        vpnData = new ArrayList<>();
        mapper = new ObjectMapper();
    }

    public List<VpnData> fillSsLinkField (String country)
    {
        outlinekeysService.getAllSsLinksByCountry(country)
                .forEach(ssLink -> {
                    VpnData vpn = VpnData.builder()
                            .ssLink(ssLink)
                            .build();

                    log.info("Start fill {} link", ssLink);
                    fillTempDirField(vpn);
                    fillHostPortFields(vpn);
                    fillPingMbpsFields(vpn);

                    vpnData.add(vpn);
                });

        cleanConfigs();
        vpnData.removeIf(data -> data.getMbps() == INVALID_MBPS);
        return vpnData;
    }

    public void fillTempDirField(VpnData vpnData)
    {
        String tempDir = createTempConfig(vpnData.getSsLink());
        vpnData.setTempDir(tempDir);
    }

    public String createTempConfig(String ssLink)
    {
        String fileName =  String.format("config_%s.json", UUID.randomUUID());
        JsonNode jsonConfig = XrayConfigConverter.linkToJson(ssLink);
        try
        {
            return XrayConfigFileUtil
                    .createConfig(
                            fileName,
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
        String jsonString = XrayConfigFileUtil.readConfig(vpnData.getTempDir());
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
        vpnData.forEach(vpnConfig -> XrayConfigFileUtil.delete(vpnConfig.getTempDir()));
    }

}
