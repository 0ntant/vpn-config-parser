package service;

import app.service.LinkProviderOutlinekeysService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LinkProviderOutlinekeysServiceIT
{
    LinkProviderOutlinekeysService service = new LinkProviderOutlinekeysService();

    @Test
    void getRussianPageSize_return4()
    {
        //given
        String countryName = "russia";
        //then

        int pageSize = service.getPageSize(countryName);
        //expected

        assertEquals(4, pageSize);
    }

    @Test
    void getUkrainePageSize_return1()
    {
        //given
        String countryName = "ukraine";
        //then

        int pageSize = service.getPageSize(countryName);
        //expected

        assertEquals(1, pageSize);
    }

    @Test
    void getRussianServersLinks_returnList20Size()
    {
        //given
        String country = "russia";
        int pageNumber = 2;

        //then
        List<String> links = service.getKeys(country, pageNumber);

        //expected
        assertEquals(20, links.size());
        System.out.printf(String.valueOf(links));
    }

    @Test
    void getRussianServersLinkValue_returnSsLinkValue()
    {
        //given
        String keyValue = "26206";

        //then
        String ssLinkValue = service.getSsLink(keyValue);

        //expected
        assertEquals(
                "ss://Y2hhY2hhMjAtaWV0Zi1wb2x5MTMwNTo3THQyc3FVdTl3c0E0bEFnUGNYZGpC@95.81.106.102:8443#Russia%20#26206%20/%20OutlineKeys.com",
                ssLinkValue
        );
    }

    @Test
    void countLinks()
    {
        //given
        List<String> links = service.getAllSsLinksByCountry("russia");
        //then

        //expected
        assertEquals(20 * 3 + 14,  links.size());
    }


}
