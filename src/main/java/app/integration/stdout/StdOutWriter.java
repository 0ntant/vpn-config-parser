package app.integration.stdout;

import app.mapper.StdoutMapper;
import app.model.VpnData;

import java.util.List;

public class StdOutWriter
{
    public void writeData(List<VpnData> dataList)
    {
        dataList
                .stream()
                .map(StdoutMapper::map)
                .forEach(vpnConf -> System.out.printf(
                                "ping: %s Mbps:%.2f ssLink: %s %n",
                                vpnConf.ping(),
                                vpnConf.mbps(),
                                vpnConf.ssLink()
                        )
                );
    }
}
