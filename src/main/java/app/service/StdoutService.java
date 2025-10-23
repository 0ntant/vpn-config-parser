package app.service;

import app.integration.stdout.dto.StdOutWriter;
import app.model.VpnData;

import java.util.Comparator;
import java.util.List;

public class StdoutService
{
    StdOutWriter writer;

    public StdoutService()
    {
        this.writer = new StdOutWriter();
    }

    public void sendVpnConf(List<VpnData> dataList)
    {
        writer.writeData(
                dataList.stream()
                        .sorted(Comparator
                                .comparingLong(VpnData::getPing)
                                .thenComparingDouble(VpnData::getMbps)
                        )
                        .toList()
        );
    }
}
