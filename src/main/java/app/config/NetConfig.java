package app.config;

public class NetConfig
{
    public static final int TIMEOUT_SECONDS =
            Integer.parseInt(
                    RootConfig
                            .appProps.getProperty("net.timeoutSeconds")
            );

    static
    {
       if (TIMEOUT_SECONDS < 1)
       {
           throw new RuntimeException("Time out must be > 1");
       }
    }
}
