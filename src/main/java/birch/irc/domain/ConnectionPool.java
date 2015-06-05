package birch.irc.domain;

import java.util.UUID;

public interface ConnectionPool {
    void connect(Connection connection);
    void send(UUID uuid, String message);
    boolean disconnect(UUID uuid);
}
