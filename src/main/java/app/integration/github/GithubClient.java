package app.integration.github;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GithubClient
{
    String url = "https://github.com/NiREvil/vless?tab=readme-ov-file#xray";

    public List<String> getXrayUris()
    {
        try
        {
            Document document = Jsoup.connect(url).get();

            logResponse(document.connection());
            return getLinksFromStartPage(document);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    public String getLinksFromPage(String url)
    {
        try
        {
            Document document = Jsoup.connect(url).get();

            logResponse(document.connection());
            return getLinksFromPage(document);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    private String getLinksFromPage(Document document)
    {
        return document.body().wholeText();
    }

    private List<String> getLinksFromStartPage(Document document)
    {
        Element start = document.selectFirst("h2:matchesOwn(^\\s*XRAY\\s*$)");
        Element end = document.selectFirst("h2:matchesOwn(^\\s*Amnezia\\s*$)");

        List<String> links = new ArrayList<>();
        boolean inside = false;

        for (Element el : document.getAllElements()) {
            if (el.equals(start)) inside = true;
            else if (el.equals(end)) inside = false;

            if (inside && el.tagName().equals("a") && el.hasAttr("href")) {
                links.add(el.attr("href"));
            }
        }

        return links;
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
