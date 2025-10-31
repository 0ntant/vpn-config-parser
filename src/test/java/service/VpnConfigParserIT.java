package service;

import app.service.VpnConfigParser;
import mock.VpnConfigParserFactoryMock;
import org.junit.jupiter.api.Test;

import java.util.List;


public class VpnConfigParserIT
{


    @Test
    public void testGitHubPage_return()
    {
        //given
        List<String> args = List.of("github",
                "CA",
                "FR",
                "GE",
                "HK",
                "JA",
                "US",
                "FI"
        );
        VpnConfigParser vpnConfigParser
                = new VpnConfigParserFactoryMock().createGitHubMockProvider(
                args,
                "https://raw.githubusercontent.com/AvenCores/goida-vpn-configs/refs/heads/main/githubmirror/1.txt");

        //then
        vpnConfigParser.printConfigs(args);
        //expected
    }

}
