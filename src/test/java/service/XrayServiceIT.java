package service;

import app.service.XrayService;
import org.junit.jupiter.api.Test;

import static app.config.DirConfig.XRAY_BIN_DIR;
import static org.junit.jupiter.api.Assertions.*;

public class XrayServiceIT
{
    XrayService service = new XrayService();

    @Test
    void xrayLifeCycle()
    {
        //given
        String config = String.format("%s/%s", XRAY_BIN_DIR, "config1.json");
        //then
        service.run(config);

        //expected
        assertTrue(service.isAlive());

        service.stop();

        assertFalse(service.isAlive());
    }
}
