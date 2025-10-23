package app.integration.outlinekeys;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class OutlinekeysClient
{
    String countyUrl = "https://outlinekeys.com/country";
    String keyUrl = "https://outlinekeys.com/key";

    public String getSsLink(String keyValue)
    {
        try
        {
            return getSsLinkValue(
                    Jsoup.connect(
                            String.format(
                                    "%s/%s/",
                                    keyUrl,
                                    keyValue)
                    ).get()
            );
        }
        catch (Exception ex)
        {
            return "";
        }
    }

    private String getSsLinkValue(Document page)
    {
        logResponse(page.connection());
        return Objects.requireNonNull(page.getElementById("accessKey")).text();
    }

    public List<String> getCountryLinks(String country, int page)
    {
        try
        {
            return linksFromPage(
                    Jsoup.connect(
                            String.format("%s/%s?page=%s",
                                    countyUrl,
                                    country,
                                    page)
                    ).get()
            );
        }
        catch (Exception ex)
        {
            return new ArrayList<>();
        }
    }

    private List<String> linksFromPage(Document page)
    {
        logResponse(page.connection());
        return page
                .getElementsByTag("h3")
                .stream()
                .map(Element::text)
                .toList();
    }

    public int getCountryPageSize(String country)
    {
        try
        {
            return pageSize(
                    Jsoup.connect(
                    String.format("%s/%s/",
                            countyUrl,
                            country)
                    ).get());
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            throw  new RuntimeException(ex);
        }
    }

    private int pageSize(Document page)
    {
       logResponse(page.connection());
       Elements pageItems = page.getElementsByClass("page-item");
       return pageItems.size() - 1;
    }

    private void logResponse(Connection connection)
    {
        log.info("[{}] {} | ResponseCode: {}",
                connection.request().method(),
                connection.request().url(),
                connection.response().statusCode()
        );
    }
}
