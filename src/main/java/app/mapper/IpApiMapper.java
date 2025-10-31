package app.mapper;

import com.fasterxml.jackson.databind.JsonNode;

public class IpApiMapper
{
    public static String mapCountryCode(JsonNode jsonNode)
    {
        return jsonNode.has("countryCode") && !jsonNode.get("countryCode").isNull()
                ? jsonNode.get("countryCode").asText()
                : "";
    }
}
