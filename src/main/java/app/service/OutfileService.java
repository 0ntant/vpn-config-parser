package app.service;

import app.model.VpnData;
import app.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

import static app.config.DirConfig.OUTFILE_DIR;

@Slf4j
public class OutfileService implements OutService
{
    @Override
    public void sendVpnConf(List<VpnData> dataList)
    {
        String ssLinksList = dataList.stream()
                .map(VpnData::getSsLink)
                .collect(Collectors.joining("\n", "\n", ""));
        Path path = Paths.get(OUTFILE_DIR);

        FileUtil.create(path, ssLinksList);
    }
}
