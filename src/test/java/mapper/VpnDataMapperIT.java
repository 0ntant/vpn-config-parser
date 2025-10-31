package mapper;

import app.mapper.VpnDataMapper;
import app.model.VpnData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;

public class VpnDataMapperIT
{
    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getPort_Host_fromJson() throws IOException
    {
        //given
        File file = new File("./src/test/resources/xrayConfig");
        //then
        VpnData vpnData = VpnDataMapper.mapHostPort(mapper.readTree(file));

        //expected
        assertEquals("81.90.31.219", vpnData.getHost());
        assertEquals("443", vpnData.getPort());
    }
}
