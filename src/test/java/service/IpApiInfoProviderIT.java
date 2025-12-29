package service;

import app.service.IpApiInfoProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IpApiInfoProviderIT
{

    IpApiInfoProvider service = new IpApiInfoProvider();

    @Test
    void getRussianPageSize_return4()
    {
        //given
        String ipAddress = "1.1.1.1";

        //then
        String country = service.getCountryByIp(ipAddress);

        //expected
        assertEquals("HK" , country);
    }
}
