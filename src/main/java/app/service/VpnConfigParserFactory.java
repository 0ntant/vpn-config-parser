package app.service;

import java.util.List;

public class VpnConfigParserFactory
{
    public VpnConfigParser create(List<String> args)
    {
        if (args == null || args.isEmpty())
        {
            throw new IllegalArgumentException("Provider required");
        }

        String providerKey = args.getFirst().toLowerCase();
        LinksProvider provider = switch (providerKey)
        {
            case "github" -> new LinkProviderGithubService();
            case "outlinekeys" -> new LinkProviderOutlinekeysService();
            default -> throw new IllegalArgumentException("Unknown provider: " + providerKey);
        };

        return new VpnConfigParser(new OutfileService(),
                provider
        );
    }
}
