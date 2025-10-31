package util;

import app.util.Base64Util;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class Base64UtilIT
{

    @Test
    void testBaseUtilFunctions() throws IOException
    {
        //given
        String base64Code = Files.readString(
                Path.of("./src/test/resources/base64Links")
        );

        //then

        //expected
        assertTrue(Base64Util.isBase64(base64Code));
        System.out.println(
                Base64Util.decode(base64Code) //.split("\n")[7].split("#")[0]
        );
    }
}
