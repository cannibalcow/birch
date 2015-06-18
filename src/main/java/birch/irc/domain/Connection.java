package birch.irc.domain;

import java.util.UUID;

public interface Connection {
    void disconnect();
    void connect();
    UUID getUUID();
    void send(String data);
    void join(String channel, String password);
}
