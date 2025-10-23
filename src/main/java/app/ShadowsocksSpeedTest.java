package app;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class ShadowsocksSpeedTest {
    public static void main(String[] args)
    {
        String serverIp = "185.192.124.188";
        int serverPort = 990;
        String socksHost = "127.0.0.1";
        int socksPort = 1080;

        long ping = testTcpPing(serverIp, serverPort);
        if (ping >= 0) {
            System.out.println("✅ TCP Ping: " + ping + " ms");
        } else {
            System.out.println("Сервер недоступен.");
        }

        try {
            System.setProperty("socksProxyHost", socksHost);
            System.setProperty("socksProxyPort", String.valueOf(socksPort));

            URL url = new URL("http://speedtest.tele2.net/5MB.zip");

            long start = System.nanoTime();
            try (InputStream in = url.openStream()) {
                byte[] buffer = new byte[8192];
                long total = 0;
                int bytesRead;

                while ((bytesRead = in.read(buffer)) != -1) {
                    total += bytesRead;
                }

                long end = System.nanoTime();
                double seconds = (end - start) / 1_000_000_000.0;
                double mbps = (total * 8 / 1_000_000.0) / seconds;
                System.out.printf("✅ Speed: %.2f Mbps%n", mbps);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long testTcpPing(String host, int port)
    {
        try
        {
            long start = System.nanoTime();
            try (Socket socket = new Socket())
            {
                socket.connect(new InetSocketAddress(host, port), 2000);
            }
            long end = System.nanoTime();
            return TimeUnit.NANOSECONDS.toMillis(end - start);
        } catch (IOException e)
        {
            return -1;
        }
    }
}
