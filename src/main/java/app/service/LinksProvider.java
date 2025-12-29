package app.service;

import java.util.List;

public interface LinksProvider
{
    List<String> getAllSsLinksByCountry(String country);
    int getPageSize(String country);
    List<String> getPageLinks(String country, int page);
}
