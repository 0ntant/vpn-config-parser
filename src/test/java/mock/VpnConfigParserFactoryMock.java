package mock;

import app.service.*;

import java.util.List;

public class VpnConfigParserFactoryMock
{
    public VpnConfigParser createGitHubMockProvider(List<String> args, String url)
    {
        if (args == null || args.isEmpty())
        {
            throw new IllegalArgumentException("Provider required");
        }

        return new VpnConfigParser(
                new OutfileService(),
                new LinkProviderGithubServiceMock(url)
        );
    }
}
