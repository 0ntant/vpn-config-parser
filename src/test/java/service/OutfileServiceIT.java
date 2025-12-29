package service;

import app.model.VpnData;
import app.service.OutfileService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OutfileServiceIT
{
    @Test
    void test_write_configs()
    {
        //given
        OutfileService outfileService = new OutfileService();
        List<VpnData> vpnDataList = List.of(
                VpnData.builder()
                        .ssLink("Link1")
                        .build(),
                VpnData.builder()
                        .ssLink("Link2")
                        .build(),
                VpnData.builder()
                        .ssLink("Link3")
                        .build()
        );

        //then
        //expected
        outfileService.sendVpnConf(vpnDataList);
    }
}
