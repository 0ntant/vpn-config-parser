package app;


import app.service.VpnConfigParser;

import java.util.ArrayList;
import java.util.Arrays;

public class Main
{
    static VpnConfigParser vpnConfigParser = new VpnConfigParser();

    public static void main(String[] args)
    {
        vpnConfigParser.printConfigs(Arrays.asList(args));
    }
}