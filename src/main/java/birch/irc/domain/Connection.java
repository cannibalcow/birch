package birch.irc.domain;

import birch.irc.IrcConnectionState;

import java.util.UUID;

public interface Connection {
    void disconnect();
    void connect();
    UUID getUUID();
    void send(String data);
    void join(String channel, String password);

    boolean connectionTimedOut();

    void reconnect();

    IrcConnectionState getState();
}
