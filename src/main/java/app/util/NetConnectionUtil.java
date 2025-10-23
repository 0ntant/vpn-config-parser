package app.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class NetConnectionUtil
{
    public static long testTcpPing(String host, int port) {
        try
        {
            long start = System.nanoTime();
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 2000);
            }
            long end = System.nanoTime();
            return TimeUnit.NANOSECONDS.toMillis(end - start);
        }
        catch (IOException e) {
            return -1;
        }
    }
}
