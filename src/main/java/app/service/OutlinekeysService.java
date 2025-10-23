package app.service;

import app.integration.outlinekeys.OutlinekeysClient;
import app.mapper.OutlinekeysMapper;

import java.util.ArrayList;
import java.util.List;

public class OutlinekeysService
{
    OutlinekeysClient client;

    public OutlinekeysService()
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

    public int getPageSize(String country)
    {
        int pageSize =  client.getCountryPageSize(country);

        return pageSize == -1 ? 1 : pageSize;
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
