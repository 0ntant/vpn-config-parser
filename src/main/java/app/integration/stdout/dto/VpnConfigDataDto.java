package app.integration.stdout.dto;

public record VpnConfigDataDto(
        long ping,
        double mbps,
        String ssLink
) { }
