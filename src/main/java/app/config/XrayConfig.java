package app.config;

public class XrayConfig
{
    public static  String XRAY_BIN_DIR = RootConfig.appProps.getProperty("xray.dir.bin");
    public static  String XRAY_TEMP_CONF_DIR = RootConfig.appProps.getProperty("xray.dir.temp.config");
}
