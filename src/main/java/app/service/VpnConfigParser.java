package app.service;

import app.model.VpnData;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Supplier;

@Slf4j
public class VpnConfigParser {

    private final OutService outService;
    private final VpnDataService vpnDataService;
    private final LinksProvider linksProvider;

    public VpnConfigParser(OutService outService, LinksProvider linksProvider)
    {
        this.outService = outService;
        this.linksProvider = linksProvider;
        this.vpnDataService = new VpnDataService(linksProvider);
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

        List<VpnData> configs = new ArrayList<>();
        log.info("Countries in filter: {}", countries);
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

        outService.sendVpnConf(sorted);
    }
}
