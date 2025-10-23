package app.service;

import app.model.VpnData;

import java.util.ArrayList;
import java.util.List;

public class VpnConfigParser
{
    StdoutService stdoutService = new StdoutService();
    VpnDataService vpnDataService = new VpnDataService();

    public void printConfigs(List<String> countries)
    {
        List<VpnData> dataConfig = new ArrayList<>();
        for(String country : countries)
        {
            dataConfig.addAll(vpnDataService.fillSsLinkField(country));
        }

        stdoutService.sendVpnConf(dataConfig);
    }
}
