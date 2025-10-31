package service;

import app.service.IpInfoProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IpInfoProviderIT
{

    IpInfoProvider service = new IpInfoProvider();

    @Test
    void getRussianPageSize_return4()
    {
        //given
        String ipAddress = "1.1.1.1";

        //then
        String country = service.getCountryByIp(ipAddress);

        //expected
        assertEquals("Australia" , country);
    }
}
