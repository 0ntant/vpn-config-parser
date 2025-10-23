package app.mapper;

import app.integration.stdout.dto.VpnConfigDataDto;
import app.model.VpnData;

public class StdoutMapper
{
    public static VpnConfigDataDto map(VpnData vpnData)
    {
        return new VpnConfigDataDto(
                vpnData.getPing(),
                vpnData.getMbps(),
                vpnData.getSsLink()
        );
    }
}
