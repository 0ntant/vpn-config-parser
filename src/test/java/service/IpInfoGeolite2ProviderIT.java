package service;

import app.service.IpInfoGeolite2Provider;
import app.service.IpInfoProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IpInfoGeolite2ProviderIT
{
    IpInfoProvider service = new IpInfoGeolite2Provider();

    @Test
    void getRussianPageSize_return4()
    {
        //given
        String ipAddress = "1.1.1.1";

        //then
        String country = service.getCountryByIp(ipAddress);

        //expected
        assertEquals("HK", country);
    }
}
