package app.integration.speedtest;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class SpeedtestClient
{
    public double getSpeed_(String socksHost, String socksPort)
    {
        try
        {
            System.setProperty("socksProxyHost", socksHost);
            System.setProperty("socksProxyPort", String.valueOf(socksPort));

            URL url = new URL("http://speedtest.tele2.net/10MB.zip");
            long start = System.nanoTime();
            try (InputStream in = url.openStream())
            {
                byte[] buffer = new byte[8192];
                long total = 0;
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    total += bytesRead;
                }
                long end = System.nanoTime();
                double seconds = (end - start) / 1_000_000_000.0;
                double mbps = (total * 8 / 1_000_000.0) / seconds;
                return mbps;
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        finally
        {
            System.clearProperty("socksProxyHost");
            System.clearProperty("socksProxyPort");
        }
    }


    public double getSpeed(String socksHost,
                               String socksPort)
    {
        try
        {
            System.setProperty("socksProxyHost", socksHost);
            System.setProperty("socksProxyPort", socksPort);

            URL url = new URL("http://speedtest.tele2.net/1MB.zip");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            long start = System.nanoTime();
            try (InputStream in = conn.getInputStream())
            {
                byte[] buffer = new byte[8192];
                long total = 0;
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    total += bytesRead;
                    if (total >= 1024 * 1024) break;
                }
                long end = System.nanoTime();
                double seconds = (end - start) / 1_000_000_000.0;
                return (total * 8 / 1_000_000.0) / seconds;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            System.clearProperty("socksProxyHost");
            System.clearProperty("socksProxyPort");
        }
    }
}
