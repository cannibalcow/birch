package birch.controller.rest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import birch.irc.IrcConnection;
import birch.irc.IrcConnectionPool;
import birch.irc.IrcLineAnalyzer;
import birch.irc.IrcMessage;
import birch.irc.domain.Connection;

@RestController
public class IrcController {

    @Autowired
    private IrcConnectionPool icp;

    @Autowired
    private IrcLineAnalyzer analyzer;

    @RequestMapping("/connect/{nick}/{server:.+}")
    public String connect(@PathVariable String nick, @PathVariable String server) {
        Connection connection = new IrcConnection(analyzer, server, nick);
        icp.connect(connection);
        return String.format("Connect to: %s with %s", nick, server);
    }

    @RequestMapping("/status")
    public List<Map<String, Object>> status() {
        return icp.getConnectionsStatus();
    }

    @RequestMapping("/disconnect/{uuid}")
    public boolean disconnect(@PathVariable String uuid) {
        UUID uid = UUID.fromString(uuid);
        icp.disconnect(uid);
        return true;
    }

    @RequestMapping("/join/{uuid}/{channel}")
    public void joinChannel(@PathVariable String uuid,
            @PathVariable String channel) {
        joinChannelWithPassword(uuid, channel, null);
    }

    @RequestMapping("/join/{uuid}/{channel}/{password}")
    public void joinChannelWithPassword(@PathVariable String uuid,
            @PathVariable String channel, @PathVariable String password) {

        icp.send(UUID.fromString(uuid),
                IrcMessage.joinChannel(channel, password));
    }
}
