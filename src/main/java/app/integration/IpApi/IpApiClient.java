package app.integration.IpApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.net.http.HttpResponse.BodyHandlers;

@Slf4j
public class IpApiClient
{
    String urlJson = "http://ip-api.com/json";
    HttpClient client;
    ObjectMapper mapper ;

    public IpApiClient()
    {
        client = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
    }

    public JsonNode getCountry(String ipAddress)
    {
       try
       {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(
                            new URI(
                                    String.format(
                                            "%s/%s",
                                            urlJson,
                                            ipAddress
                                    )
                            )
                    )
                    .GET()
                    .build();
           HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
           logResponse(request, response);

           return mapper.readTree(response.body());
       }
       catch (Exception ex)
       {
           log.error(ex.getMessage());
           throw new RuntimeException(ex);
       }
    }

    private void logResponse(HttpRequest request,
                             HttpResponse<String> response)
    {
        log.info("[{}] {} | ResponseCode: {}",
                request.method(),
                request.uri(),
                response.statusCode()
        );
    }

}
