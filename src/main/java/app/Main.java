package app;

import app.service.VpnConfigParser;
import app.service.VpnConfigParserFactory;

import java.util.Arrays;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        VpnConfigParser vpnConfigParser = new VpnConfigParserFactory()
                .create(List.of(args));
        vpnConfigParser.printConfigs(Arrays.asList(args));
    }
}