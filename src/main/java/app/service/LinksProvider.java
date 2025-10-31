package app.service;

import java.util.List;

public interface LinksProvider
{
    List<String> getAllSsLinksByCountry(String country);
}
