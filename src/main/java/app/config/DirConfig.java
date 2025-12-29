package app.config;

public class DirConfig
{
    public static  String XRAY_BIN_DIR = RootConfig.appProps.getProperty("xray.dir.bin");
    public static  String XRAY_TEMP_CONF_DIR = RootConfig.appProps.getProperty("xray.dir.temp.config");
    public static  String OUTFILE_DIR = RootConfig.appProps.getProperty("dir.out.file");
    public static  String OUTFILE_NO_DUPLICATE_DIR = RootConfig.appProps.getProperty("dir.out.file_no_duplicate");
    public static  String GEOIP2_FILE = RootConfig.appProps.getProperty("dir.geoip2.file");
}
