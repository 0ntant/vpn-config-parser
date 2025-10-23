package app.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static app.config.ProxyConfig.PROXY_HOST;
import static app.config.ProxyConfig.PROXY_PORT;

public class XrayConfigConverter {
    static final ObjectMapper mapper = new ObjectMapper();

    private static final Set<Integer> TLS_PORTS = Set.of(443, 8443, 2053, 2096, 2087, 2083);

    public static JsonNode linkToJson(String link) {
        try {
            if (link.startsWith("ss://")) {
                return parseShadowsocks(link);
            } else if (link.startsWith("vless://")) {
                return parseVless(link);
            } else if (link.startsWith("trojan://")) {
                return parseTrojan(link);
            } else {
                throw new IllegalArgumentException("Unsupported link type: " + link);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // ---------------------------
    // Shadowsocks
    // ---------------------------
    private static JsonNode parseShadowsocks(String ssLink) throws Exception {
        String body = ssLink.substring(5);
        String[] parts = body.split("#", 2);
        String mainPart = parts[0];
        String tag = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "shadowsocks";

        if (!mainPart.contains("@"))
            throw new IllegalArgumentException("Invalid Shadowsocks link format");

        String[] split = mainPart.split("@", 2);
        String encoded = split[0];
        String addressPart = split[1];

        // decode base64 credentials
        String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        String[] creds = decoded.split(":", 2);
        String method = creds[0];
        String password = creds[1];

        String[] addrParts = addressPart.split(":", 2);
        String address = addrParts[0];
        int port = Integer.parseInt(addrParts[1]);

        ObjectNode root = mapper.createObjectNode();
        root.set("inbounds", createDefaultInbound());
        root.set("outbounds", createShadowsocksOutbound(address, port, method, password, tag));
        return root;
    }

    // ---------------------------
    // VLESS
    // ---------------------------
    private static JsonNode parseVless(String vlessLink) {
        try {
            String body = vlessLink.substring(8);
            String[] parts = body.split("#", 2);
            String mainPart = parts[0];
            String tag = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "vless";

            String[] split = mainPart.split("@", 2);
            if (split.length != 2)
                throw new IllegalArgumentException("Invalid VLESS link format");

            String uuid = split[0];
            String[] hostParts = split[1].split("\\?", 2);
            String[] addrParts = hostParts[0].split(":", 2);
            if (addrParts.length != 2)
                throw new IllegalArgumentException("Invalid address part in VLESS link");

            String address = addrParts[0];
            int port = Integer.parseInt(addrParts[1]);

            // query params
            String query = hostParts.length > 1 ? hostParts[1] : "";
            Map<String, String> params = new HashMap<>();
            if (!query.isEmpty()) {
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2) params.put(kv[0], kv[1]);
                }
            }

            // Security detection
            String security = params.getOrDefault("security",
                    TLS_PORTS.contains(port) ? "tls" : "none").toLowerCase();

            // Inject default flow for Reality
            if ("reality".equals(security)) {
                params.putIfAbsent("flow", "xtls-rprx-vision");
            }

            params.putIfAbsent("security", security);

            ObjectNode root = mapper.createObjectNode();
            root.set("inbounds", createDefaultInbound());
            root.set("outbounds", createVlessOutbound(address, port, uuid, tag, params));
            return root;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse VLESS link", e);
        }
    }

    private static ArrayNode createVlessOutbound(String address, int port, String uuid, String tag, Map<String, String> params) {
        ArrayNode outbounds = mapper.createArrayNode();
        ObjectNode outbound = mapper.createObjectNode();
        outbound.put("protocol", "vless");

        // Settings
        ObjectNode settings = mapper.createObjectNode();
        ArrayNode vnext = mapper.createArrayNode();
        ObjectNode server = mapper.createObjectNode();
        server.put("address", address);
        server.put("port", port);

        ArrayNode users = mapper.createArrayNode();
        ObjectNode user = mapper.createObjectNode();
        user.put("id", uuid);
        user.put("encryption", "none");
        if (params.containsKey("flow")) user.put("flow", params.get("flow"));
        users.add(user);

        server.set("users", users);
        vnext.add(server);
        settings.set("vnext", vnext);
        outbound.set("settings", settings);

        outbound.put("tag", tag.isEmpty() ? "vless-out" : tag);

        // Stream settings
        ObjectNode streamSettings = mapper.createObjectNode();
        String network = params.getOrDefault("type", "tcp").toLowerCase();
        streamSettings.put("network", network);

        String security = params.getOrDefault("security",
                TLS_PORTS.contains(port) ? "tls" : "none").toLowerCase();
        streamSettings.put("security", security);

        if ("ws".equals(network)) {
            ObjectNode wsSettings = mapper.createObjectNode();
            wsSettings.put("path", URLDecoder.decode(params.getOrDefault("path", "/"), StandardCharsets.UTF_8));
            ObjectNode headers = mapper.createObjectNode();
            headers.put("Host", params.getOrDefault("host", params.getOrDefault("sni", address)));
            wsSettings.set("headers", headers);
            streamSettings.set("wsSettings", wsSettings);
        }

        // TLS / REALITY settings
        if ("tls".equals(security)) {
            ObjectNode tlsSettings = mapper.createObjectNode();
            tlsSettings.put("allowInsecure", true);
            tlsSettings.put("serverName", params.getOrDefault("sni", params.getOrDefault("host", address)));
            streamSettings.set("tlsSettings", tlsSettings);
        } else if ("reality".equals(security)) {
            ObjectNode realitySettings = mapper.createObjectNode();
            realitySettings.put("serverName", params.getOrDefault("sni", address));
            realitySettings.put("fingerprint", params.getOrDefault("fp", "chrome"));
            streamSettings.set("realitySettings", realitySettings);
        }

        outbound.set("streamSettings", streamSettings);
        outbounds.add(outbound);
        return outbounds;
    }

    // ---------------------------
    // TROJAN
    // ---------------------------
    private static JsonNode parseTrojan(String trojanLink) {
        try {
            String body = trojanLink.substring(9);
            String[] parts = body.split("#", 2);
            String mainPart = parts[0];
            String tag = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "trojan";

            String[] split = mainPart.split("@");
            if (split.length != 2)
                throw new IllegalArgumentException("Invalid Trojan link format");

            String password = split[0];

            String hostPortPart;
            String query = "";
            if (split[1].contains("?")) {
                String[] hostSplit = split[1].split("\\?", 2);
                hostPortPart = hostSplit[0];
                query = hostSplit[1];
            } else {
                hostPortPart = split[1];
            }

            String[] addrParts = hostPortPart.split(":", 2);
            String address = addrParts[0];
            int port = Integer.parseInt(addrParts[1]);

            Map<String, String> params = new HashMap<>();
            if (!query.isEmpty()) {
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2) params.put(kv[0], kv[1]);
                }
            }

            ObjectNode root = mapper.createObjectNode();
            root.set("inbounds", createDefaultInbound());
            root.set("outbounds", createTrojanOutbound(address, port, password, tag, params));
            return root;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Trojan link", e);
        }
    }

    private static ArrayNode createTrojanOutbound(String address, int port, String password, String tag, Map<String, String> params) {
        ArrayNode outbounds = mapper.createArrayNode();
        ObjectNode outbound = mapper.createObjectNode();
        outbound.put("protocol", "trojan");

        ObjectNode settings = mapper.createObjectNode();
        ArrayNode servers = mapper.createArrayNode();
        ObjectNode server = mapper.createObjectNode();
        server.put("address", address);
        server.put("port", port);
        server.put("password", password);
        servers.add(server);
        settings.set("servers", servers);

        outbound.set("settings", settings);
        outbound.put("tag", tag);

        ObjectNode streamSettings = mapper.createObjectNode();
        String network = params.getOrDefault("type", "tcp");
        String security = params.getOrDefault("security", TLS_PORTS.contains(port) ? "tls" : "none");
        streamSettings.put("network", network);
        streamSettings.put("security", security);

        if ("ws".equalsIgnoreCase(network)) {
            ObjectNode wsSettings = mapper.createObjectNode();
            wsSettings.put("path", URLDecoder.decode(params.getOrDefault("path", "/"), StandardCharsets.UTF_8));
            ObjectNode headers = mapper.createObjectNode();
            headers.put("Host", params.getOrDefault("host", address));
            wsSettings.set("headers", headers);
            streamSettings.set("wsSettings", wsSettings);
        }

        if ("tls".equalsIgnoreCase(security)) {
            ObjectNode tlsSettings = mapper.createObjectNode();
            tlsSettings.put("allowInsecure", true);
            tlsSettings.put("serverName", params.getOrDefault("sni", address));
            streamSettings.set("tlsSettings", tlsSettings);
        }

        outbound.set("streamSettings", streamSettings);
        outbounds.add(outbound);
        return outbounds;
    }

    // ---------------------------
    // Common helpers
    // ---------------------------
    private static ArrayNode createDefaultInbound() {
        ArrayNode inbounds = mapper.createArrayNode();
        ObjectNode inbound = mapper.createObjectNode();
        inbound.put("port", PROXY_PORT);
        inbound.put("listen", PROXY_HOST);
        inbound.put("protocol", "socks");

        ObjectNode inSettings = mapper.createObjectNode();
        inSettings.put("udp", true);
        inbound.set("settings", inSettings);

        inbounds.add(inbound);
        return inbounds;
    }

    private static ArrayNode createShadowsocksOutbound(String address, int port, String method, String password, String tag) {
        ArrayNode outbounds = mapper.createArrayNode();
        ObjectNode outbound = mapper.createObjectNode();
        outbound.put("protocol", "shadowsocks");

        ObjectNode settings = mapper.createObjectNode();
        ArrayNode servers = mapper.createArrayNode();
        ObjectNode server = mapper.createObjectNode();
        server.put("address", address);
        server.put("port", port);
        server.put("method", method);
        server.put("password", password);
        servers.add(server);
        settings.set("servers", servers);

        outbound.set("settings", settings);
        outbound.put("tag", tag);
        outbounds.add(outbound);
        return outbounds;
    }
}
