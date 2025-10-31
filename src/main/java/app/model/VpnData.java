package app.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VpnData
{
    long ping;
    double mbps;
    String tempDir;
    String ssLink;
    String port;
    String host;

    VpnDataError errorStatus;
}
