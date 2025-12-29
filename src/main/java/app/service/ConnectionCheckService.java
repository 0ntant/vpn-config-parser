package app.service;

import app.model.VpnData;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

import static app.config.CheckStatusConfig.INVALID_MBPS;
import static app.config.NetConfig.TIMEOUT_SECONDS;
import static app.config.ProcessStatusConfig.RUN_ERROR;
import static app.config.ProcessStatusConfig.RUN_TIMEOUT;

@Slf4j
public class ConnectionCheckService
{
    SpeedtestService speedtestService;
    NetConnectionService netConnectionService;
    XrayService xrayService;

    public ConnectionCheckService()
    {
        speedtestService = new SpeedtestService();
        netConnectionService=  new NetConnectionService();
        xrayService= new XrayServiceImp();
    }

    public void fillFields(VpnData vpnData)
    {
        fillPingField(vpnData);
        fillMbpsField(vpnData);
    }

    public void fillPingField(VpnData vpnData)
    {
        long pingValue = netConnectionService.testTcpPing(
                 vpnData.getHost(),
                 Integer.parseInt(vpnData.getPort())
        );

        log.info("Host: {} Port: {} Ping: {} ms",
                vpnData.getHost(),
                vpnData.getPort(),
                pingValue
        );
        vpnData.setPing(pingValue);
    }

    public void fillMbpsField(VpnData vpnData) {
        int xrayStatus = xrayService.runXray(vpnData.getTempDir());
        double mbpsValue;

        if (xrayStatus == RUN_TIMEOUT || xrayStatus == RUN_ERROR)
        {
            mbpsValue = INVALID_MBPS;
            log.error("Xray errorStart code: {}", xrayStatus);
        }
        else
        {
            mbpsValue = getMbps();
            log.info("Host: {} Port: {} Mbps: {}",
                    vpnData.getHost(),
                    vpnData.getPort(),
                    mbpsValue
            );
        }

        vpnData.setMbps(mbpsValue);
        xrayService.stop();
    }


    private double getMbps()
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Double> future = executor.submit(() -> speedtestService.getSpeed());
        int secTimeout = TIMEOUT_SECONDS;
        try
        {
            return future.get(secTimeout, TimeUnit.SECONDS);
        }
        catch (TimeoutException e)
        {
            log.warn("Timeout getMbps > {} seconds", secTimeout);
            future.cancel(true);
            return INVALID_MBPS;
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            return INVALID_MBPS;
        }
        finally
        {
            executor.shutdownNow();
        }
    }
}
