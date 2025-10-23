package app.service;

import app.integration.speedtest.SpeedtestClient;

import static app.config.ProxyConfig.PROXY_HOST;
import static app.config.ProxyConfig.PROXY_PORT;

public class SpeedtestService
{
    SpeedtestClient client;

    public SpeedtestService()
    {
        this.client = new SpeedtestClient();
    }
    public double getSpeed()
    {
        return client.getSpeed(PROXY_HOST, String.valueOf(PROXY_PORT));
    }
}
