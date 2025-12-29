package mock;

import app.service.LinkProviderGithubService;
import app.service.LinksProvider;

import java.util.List;

public class LinkProviderGithubServiceMock
        implements LinksProvider
{
    LinkProviderGithubService provider = new LinkProviderGithubService();
    String gitHubUrl;

    public LinkProviderGithubServiceMock(String url)
    {
        gitHubUrl= url;
    }

    @Override
    public List<String> getAllSsLinksByCountry(String country)
    {
        return  provider.filterLink(
                provider.getLinksFromUrl(gitHubUrl) ,
                country) ;
    }

    @Override
    public int getPageSize(String country) {
        return provider.filterLink(
                provider.getLinksFromUrl(gitHubUrl) ,
                country).size();
    }

    @Override
    public List<String> getPageLinks(String country, int page)
    {
        return provider.getPageLinks(country,page);
    }

    public List<String> getLinksFromUrl(String url)
    {
        return provider.getLinksFromUrl(gitHubUrl);
    }
}
