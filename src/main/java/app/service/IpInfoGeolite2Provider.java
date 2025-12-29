package app.service;

import app.config.DirConfig;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.InetAddress;

@Slf4j
public class IpInfoGeolite2Provider implements IpInfoProvider
{
    private final IpApiInfoProvider ipApiInfoProvider;

    public IpInfoGeolite2Provider()
    {
        ipApiInfoProvider = new IpApiInfoProvider();
    }

    @Override
    public String getCountryByIp(String ipAddress)
    {
        try
        {
            File database = new File(DirConfig.GEOIP2_FILE);
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
            CountryResponse country = dbReader.country(
                    InetAddress.getByName(ipAddress)
            );

            String countryCode = country
                .country()
                .isoCode();

            if(countryCode == null
                    || countryCode.isBlank())
            {
                log.warn("No ip={} in local base using api", ipAddress);
                countryCode = ipApiInfoProvider.getCountryByIp(ipAddress);
            }
            log.info("IP={} is {}", ipAddress, countryCode);

            return countryCode;
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            return "INVALID_ADDRESS";
        }
    }
}
