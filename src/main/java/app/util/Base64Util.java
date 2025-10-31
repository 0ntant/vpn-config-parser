package app.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util
{
    public static boolean isBase64(String str)
    {
        if (str == null || str.isEmpty()) return false;
        try
        {
            decode(str);
            return true;
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public static String decode(String baseString)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(baseString);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
