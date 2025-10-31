package app.util;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class FileUtil
{
    public static String create(Path pathToFile, String data)
    {
        try
        {
            Files.write(pathToFile, data.getBytes());
            return pathToFile.toString();
        }
        catch (Exception ex)
        {
            throw  new RuntimeException(ex);
        }
    }

    public static String read(String pathToFile) {
        Path path = Paths.get(pathToFile);
        try
        {
            return Files.readString(path);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isExists(String pathToFile)
    {
        return Files.exists(Paths.get(pathToFile));
    }

    public static boolean delete(String pathToFile)
    {
        try
        {
            return Files.deleteIfExists(Paths.get(pathToFile));
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    public static List<Path> getRegularFiles(String dir)
    {
        try (Stream<Path> paths = Files.list(Paths.get(dir)))
        {
            return paths
                    .filter(Files::isRegularFile)
                    .toList();
        }
        catch (Exception ex)
        {
            throw  new RuntimeException(ex);
        }
    }
}
