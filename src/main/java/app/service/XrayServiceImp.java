package app.service;
import app.util.XrayUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XrayServiceImp implements XrayService
{
    @Override
    public int runXray(String config)
    {
        log.info("Run xray config={}", config);
        return XrayUtil.run(config);
    }

    @Override
    public boolean isAlive()
    {
        return XrayUtil.isAlive();
    }

    @Override
    public void stop()
    {
        log.info("Stop xray");
        XrayUtil.stop();
    }
}
