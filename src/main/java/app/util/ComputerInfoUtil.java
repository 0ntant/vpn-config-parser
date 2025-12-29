package app.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import static app.config.ProxyConfig.PROXY_HOST;

public class ComputerInfoUtil
{
    public static int getLogicalCores()
    {
        return Runtime.getRuntime().availableProcessors();
    }

    public static List<Integer> findFreePorts(int countNeeded)
    {
        int startPort = 8080;
        List<Integer> freePorts = new ArrayList<>();

        for (int port = startPort; port <= 65535 && freePorts.size() < countNeeded; port++)
        {
            if (isPortFree(port)) {
                freePorts.add(port);
            }
        }

        return freePorts;
    }

    private static boolean isPortFree(int port)
    {
        try (ServerSocket socket = new ServerSocket())
        {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(PROXY_HOST, port));
            return true;
        } catch (IOException e)
        {
            return false;
        }
    }
}
