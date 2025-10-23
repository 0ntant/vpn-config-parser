package app.config;

public class ProxyConfig
{
    public static int PROXY_PORT = Integer.parseInt(RootConfig.appProps.getProperty("proxy.port"));
    public static String PROXY_HOST = RootConfig.appProps.getProperty("proxy.host");
}
