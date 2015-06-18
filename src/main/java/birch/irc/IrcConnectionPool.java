package birch.irc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import birch.irc.domain.Connection;
import birch.irc.domain.ConnectionPool;

@Component
public class IrcConnectionPool implements ConnectionPool {
    Logger log = Logger.getLogger(IrcConnectionPool.class);
    private Set<Connection> connections;

    public IrcConnectionPool() {
        this.connections = new HashSet<Connection>();
    }

    @Override
    public void connect(Connection connection) {
        this.connections.add(connection);
        if (connection instanceof IrcConnection) {
            Thread thread = new Thread((IrcConnection) connection);
            thread.setName(((IrcConnection) connection).getNick() + "_" +((IrcConnection) connection).getServer());
            thread.start();
        }
    }

    @Override
    public boolean disconnect(UUID uuid) {
        log.info("Disconnecting " + uuid);
        Connection connection = getConnection(uuid);
        if(connection != null) {
            connection.disconnect();
            connections.remove(connection);
            return true;
        } else { 
            return false;
        }
    }

    @Override
    public void send(UUID uuid, String message) {
        getConnection(uuid).send(message);
    }

    private Connection getConnection(UUID uuid) {
        Optional<Connection> result = connections.stream()
                .filter(con -> con.getUUID().equals(uuid)).findFirst();

        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public List<Map<String, Object>> getConnectionsStatus() {
        List<Map<String, Object>> status = new ArrayList<Map<String, Object>>();
        connections.forEach(con -> status
                .add(createStatusInfo((IrcConnection) con)));
        return status;
    }

    private Map<String, Object> createStatusInfo(IrcConnection con) {
        Map<String, Object> status = new HashMap<String, Object>();
        status.put("uuid", con.getUUID().toString());
        status.put("nick", con.getNick());
        status.put("server", con.getServer());
        status.put("isConnected", con.isConnected());
        status.put("isRegisterd", con.isRegisterd());
        status.put("channels", con.getChannels());
        return status;
    }

    public void join(UUID uuid, String channel, String password) {
        Connection con = getConnection(uuid);
        con.join(channel, password);
    }

    
}
