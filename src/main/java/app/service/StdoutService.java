package app.service;

import app.integration.stdout.StdOutWriter;
import app.model.VpnData;

import java.util.Comparator;
import java.util.List;

public class StdoutService implements OutService
{
    StdOutWriter writer;

    public StdoutService()
    {
        this.writer = new StdOutWriter();
    }

    @Override
    public void sendVpnConf(List<VpnData> dataList)
    {
        writer.writeData(
               dataList
        );
    }
}
