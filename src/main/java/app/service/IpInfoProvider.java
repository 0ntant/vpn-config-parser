package app.service;

import app.integration.IpApi.IpApiClient;
import app.mapper.IpApiMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class IpInfoProvider
{
    private final IpApiClient client;
    private final Map<String, String> cache = new ConcurrentHashMap<>();
    private long lastRequestTime = 0;
    private final long minIntervalMs = 60_000 / 45;
    private final Object lock = new Object();

    public IpInfoProvider()
    {
        client = new IpApiClient();
    }

    public String getCountryByIp(String ipAddress) {

        if (cache.containsKey(ipAddress))
        {
            return cache.get(ipAddress);
        }

        synchronized (lock)
        {
            long now = System.currentTimeMillis();
            long wait = minIntervalMs - (now - lastRequestTime);
            if (wait > 0) {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            String country = IpApiMapper.mapCountryCode(client.getCountry(ipAddress));
            cache.put(ipAddress, country);
            lastRequestTime = System.currentTimeMillis();

            log.info("IP={} is {}", ipAddress, country);
            return country;
        }
    }
}
