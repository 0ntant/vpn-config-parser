package app.mapper;

import app.model.VpnData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class VpnDataMapper
{
    public static VpnData mapHostPort(JsonNode json)
    {
        JsonNode server = json.path("outbounds").get(0).path("settings");

        JsonNode target = null;

        if (server.has("servers")) {
            target = server.path("servers").get(0);
        } else if (server.has("vnext")) {
            target = server.path("vnext").get(0);
        }

        if (target == null) {
            throw new IllegalArgumentException("JSON does not contain a supported server structure");
        }

        String address = target.path("address").asText();
        int port = target.path("port").asInt();

        return VpnData.builder()
                .host(address)
                .port(String.valueOf(port))
                .build();
    }
}
