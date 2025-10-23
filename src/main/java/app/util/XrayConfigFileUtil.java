package app.util;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static app.config.XrayConfig.XRAY_TEMP_CONF_DIR;

public class XrayConfigFileUtil
{
    public static String createConfig(String name, String data)
    {
        Path configPath = Paths.get(String.format("%s/%s", XRAY_TEMP_CONF_DIR, name));
        try
        {
            Files.write(configPath, data.getBytes());
            return configPath.toString();
        }
        catch (Exception ex)
        {
            throw  new RuntimeException(ex);
        }
    }

    public static String readConfig(String pathToFile) {
        Path path = Paths.get(pathToFile);
        try
        {
            return Files.readString(path);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isFileExists(String pathToFile)
    {
        return Files.exists(Paths.get(pathToFile));
    }

    public static void delete(String pathToFile)
    {
        try
        {
            Files.deleteIfExists(Paths.get(pathToFile));
        }
        catch (Exception ex)
        {
            throw  new RuntimeException(ex);
        }
    }
}
