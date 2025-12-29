package app.service;

import app.model.VpnData;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static app.config.DirConfig.OUTFILE_DIR;
import static app.config.DirConfig.OUTFILE_NO_DUPLICATE_DIR;

@Slf4j
public class VpnConfigParser
{
    private final OutService outService;
    private final VpnDataService vpnDataService;
    private final OutfileRewriter outfileRewriter;

    public VpnConfigParser(OutService outService, LinksProvider linksProvider)
    {
        this.outService = outService;
        this.vpnDataService = new VpnDataService(linksProvider);
        this.outfileRewriter= new OutfileRewriter();
    }

    public void printConfigs(List<String> args)
    {
        if (args == null || args.size() < 2) {
            throw new IllegalArgumentException("Provider and countries required");
        }

        List<String> countries = args.subList(1, args.size()).stream()
                .takeWhile(arg -> !arg.startsWith("-"))
                .toList();

        if (countries.isEmpty()) {
            throw new IllegalArgumentException("At least one country required");
        }

        log.info("Countries in filter: {}", countries);

        asyncPrinted(countries);
        outfileRewriter.writeUniqueIpLinks();
        log.info("Write unique links in={}", OUTFILE_NO_DUPLICATE_DIR);
    }

    private void syncPrinted(List<String> countries)
    {
        List<VpnData> configs = new ArrayList<>();
        for (String country : countries)
        {
            configs.addAll(vpnDataService.fillSsLinkField(country));
        }

        List<VpnData> sorted = configs.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator
                        .comparingLong(VpnData::getPing)
                        .thenComparingDouble(VpnData::getMbps))
                .distinct()
                .toList();

        log.info("Write all links in file: {}", OUTFILE_DIR);
        outService.sendVpnConf(sorted);
    }

    private void asyncPrinted(List<String> countries)
    {
        for (String country : countries)
        {
            vpnDataService.asyncWriteInfoFile(country);
        }
    }
}
