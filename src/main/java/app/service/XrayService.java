package app.service;
import app.util.XrayUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XrayService
{
    public int run(String config)
    {
        log.info("Run xray config={}", config);
        return XrayUtil.run(config);
    }

    public boolean isAlive()
    {
        return XrayUtil.isAlive();
    }

    public void stop()
    {
        log.info("Stop xray");
        XrayUtil.stop();
    }
}
