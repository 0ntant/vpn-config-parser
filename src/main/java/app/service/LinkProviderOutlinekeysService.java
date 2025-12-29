package app.service;

import app.integration.outlinekeys.OutlinekeysClient;
import app.mapper.OutlinekeysMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LinkProviderOutlinekeysService implements LinksProvider
{
    OutlinekeysClient client;

    public LinkProviderOutlinekeysService()
    {
        this.client = new OutlinekeysClient();
    }

    public List<String> getAllSsLinksByCountry(String country)
    {
        List<String> ssLinks = new ArrayList<>();
        int countryPageSize = getPageSize(country);

        for (int i = 1; i < countryPageSize + 1; i++)
        {
            List<String> keys = getKeys(country, i);

            for (String key : keys)
            {
                ssLinks.add(getSsLink(key));
            }
        }

        return ssLinks;
    }

    @Override
    public int getPageSize(String country)
    {
        int pageSize;
        try
        {
            pageSize =  client.getCountryPageSize(country);
        }
        catch (Exception ex)
        {
            pageSize = -1;
            log.warn("Outlinekeys site error: {}" , ex.getMessage());
        }

        return pageSize == -1 ? 1 : pageSize;
    }

    @Override
    public List<String> getPageLinks(String country, int page)
    {
        return client.getCountryLinks(country, page)
                .stream()
                .map(OutlinekeysMapper::linkFromCountryWithLink)
                .map(client::getSsLink)
                .toList();
    }

    public List<String> getKeys(String country, int page)
    {
        return client.getCountryLinks(country, page)
                .stream()
                .map(OutlinekeysMapper::linkFromCountryWithLink)
                .toList();
    }

    public String getSsLink(String keyValue)
    {
        return client.getSsLink(keyValue);
    }
}
