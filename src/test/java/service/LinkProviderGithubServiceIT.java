package service;

import app.service.LinkProviderGithubService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinkProviderGithubServiceIT
{

    LinkProviderGithubService service = new LinkProviderGithubService();
    @Test
    void getGithubXray_returnStartPageLinks()
    {
        //give

        //then
        List<String> links = service.getXrayUris();

        //expected
        assertFalse(links.isEmpty());
        for (String link : links)
        {
            System.out.println(link);
        }
    }

    @Test
    void getGithubXrayLink_returnsSLinks()
    {
        //given
        String url = "https://raw.githubusercontent.com/AvenCores/goida-vpn-configs/refs/heads/main/githubmirror/1.txt";

        //then
        List<String> links = service.getLinksFromUrl(url);

        //expected
        assertFalse(links.isEmpty());
        for (String link : links)
        {
            System.out.println(link);
        }
    }

    @Test
    void getGithubXray_returnLinksLinks()
    {
        //given

        //then

        for (String url : service.getXrayUris())
        {
            List<String> links =  service.getLinksFromUrl(url);

            for(String link : links)
            {
                System.out.println(link);
            }
        }
        //expected


    }

}
