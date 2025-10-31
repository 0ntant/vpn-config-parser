package service;

import app.model.VpnData;
import app.service.VpnDataService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VpnDataServiceIT
{
    VpnDataService service = new VpnDataService();

    @Test
    void testFillFields()
    {
        //given
        String country = "russia";

        //then
        List<VpnData> vpnData = service.fillSsLinkField(country);

        //expected
        System.out.printf("Valid config count: %s", vpnData.size());
    }
}
