package app.service;

import app.integration.github.GithubClient;
import app.util.Base64Util;
import app.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class LinkProviderGithubService implements LinksProvider
{
    IpInfoProvider ipInfoProvider ;
    GithubClient client;

    public LinkProviderGithubService()
    {
        ipInfoProvider = new IpInfoProvider();
        client = new GithubClient();
    }

    @Override
    public List<String> getAllSsLinksByCountry(String country)
    {
       List<String> xrayUris = getXrayUris();
       List<String> links = new ArrayList<>() ;

       for (String xrayUri : xrayUris)
       {
           links.addAll(
                   filterLink(getLinksFromUrl(xrayUri) , country)
           );
       }

       return links;
    }

    public List<String> getLinksFromUrl(String url)
    {
        String pageData;
        try
        {
            pageData = client.getLinksFromPage(url);
        }
        catch (Exception ex)
        {
            log.warn("Github error skip url={}", url);
            return List.of();
        }

        if (Base64Util.isBase64(pageData))
        {
            pageData = Base64Util.decode(pageData);
        }

        List<String> links = Arrays.asList(pageData.split("\n"));
        return links.stream()
                .filter(link -> link.startsWith("ss://") ||
                        link.startsWith("vless://") ||
                        link.startsWith("trojan://")
                )
                .map(link -> link.split("#")[0])
                .toList();
    }

    public List<String> filterLink(List<String> links, String country)
    {
        return links.stream()
                .filter(link -> StringUtil.extractFirstIpv4(link).isPresent())
                .filter(link -> ipInfoProvider
                        .getCountryByIp(StringUtil
                                .extractFirstIpv4(link).get())
                        .equalsIgnoreCase(country))
                .toList();

    }

    public List<String> getXrayUris()
    {
        return
                client.getXrayUris().stream()
                        .skip(3)
                        .filter(s -> s.contains("raw.") || s.endsWith(".txt"))
                        .toList();
    }
}
