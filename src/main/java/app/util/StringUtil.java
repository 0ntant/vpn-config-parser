package app.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil
{
    public static Optional<String> extractIpByUrl(String url)
    {
        try
        {
            URI uri = new URI(url);
            String host = uri.getHost();
            return Optional.ofNullable(host);
        }
        catch (URISyntaxException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> extractFirstIpv4(String input)
    {
        if (input == null) return Optional.empty();

        String ipv4Regex = "\\b(?:(?:25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}"
                + "(?:25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\b";

        Pattern p = Pattern.compile(ipv4Regex);
        Matcher m = p.matcher(input);
        if (m.find()) {
            return Optional.of(m.group());
        }
        return Optional.empty();
    }
}
