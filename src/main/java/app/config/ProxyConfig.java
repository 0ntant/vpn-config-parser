package app.config;

import app.util.ComputerInfoUtil;

public class ProxyConfig
{
    public static int PROXY_PORT = Integer.parseInt(RootConfig.appProps.getProperty("proxy.port"));
    public static String PROXY_HOST = RootConfig.appProps.getProperty("proxy.host");
//    public static int PROCESS_COUNT = Integer.parseInt(RootConfig.appProps.getProperty("xray.process.count")) != 0
//            ? Integer.parseInt(RootConfig.appProps.getProperty("xray.process.count"))
//            : ComputerInfoUtil.getLogicalCores() - 1 ;
}
