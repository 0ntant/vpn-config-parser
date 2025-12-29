package app.service;

import app.util.FileUtil;
import app.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.lang.String;

import static app.config.DirConfig.OUTFILE_DIR;
import static app.config.DirConfig.OUTFILE_NO_DUPLICATE_DIR;

@Slf4j
public class OutfileRewriter
{
    public void writeUniqueIpLinks()
    {
        List<String> links = Arrays.stream(FileUtil.read(OUTFILE_DIR).split("\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();

        List<String> uniqueLinks = StringUtil.dedupByIp(links);

        if (!uniqueLinks.isEmpty()) {
            FileUtil.create(
                    Path.of(OUTFILE_NO_DUPLICATE_DIR),
                    String.join("\n", uniqueLinks)
            );
        }
    }
}
