package util;

import app.util.StringUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilIT
{

    @Test
    void testStringUtilFunctions() throws IOException
    {
        //given
        String linksFromFile = Files.readString(
                Path.of("./src/test/resources/vlessLinks")
        );

        //then

        List<Optional<String>> links = Arrays.stream(linksFromFile.split("\n"))
                .map(StringUtil::extractFirstIpv4)
                .toList();

        //expected
        assertFalse(links.isEmpty());

        for (Optional<String> link : links)
        {
            link.ifPresent(System.out::println);
        }

    }
}
