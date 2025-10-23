package app.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class RootConfig
{
    static final Properties appProps = new Properties();
    static
    {
        try (FileInputStream input = new FileInputStream("config.properties"))
        {
            appProps.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
