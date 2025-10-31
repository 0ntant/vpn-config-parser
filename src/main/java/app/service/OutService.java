package app.service;

import app.model.VpnData;

import java.util.List;

public interface OutService
{
    void sendVpnConf(List<VpnData> dataList);
}
