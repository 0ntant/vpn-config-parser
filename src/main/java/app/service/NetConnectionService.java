package app.service;

import app.util.NetConnectionUtil;

public class NetConnectionService
{
    public long testTcpPing(String host, int port)
    {
        return NetConnectionUtil.testTcpPing(host, port);
    }
}
