package birch.irc;

import birch.irc.domain.Connection;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.Seconds;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static birch.irc.IrcConnectionState.CONNECTED;
import static birch.irc.IrcConnectionState.DISCONNECTED;

// Todo. rewrite this shit
public class IrcConnection implements Connection, Runnable {
    private static final int CONNECTION_TIMEOUT = 10;
    Logger log = Logger.getLogger(IrcConnection.class);
    private UUID uuid;
    private String server;
    private Socket socket;
    private InputStream is;
    private boolean registerd;
    private String nick;
    private PrintWriter writer;
    private String user;
    private String realName;
    private Integer mode = 0;
    private IrcLineAnalyzer analyzer;
    private Set<IrcChannel> channels = new HashSet<>();
    private Date lastPing;
    private IrcConnectionState connectionState = DISCONNECTED;

    public IrcConnectionState getConnectionState() {
        return connectionState;
    }

    public Set<IrcChannel> getChannels() {
        return channels;
    }

    public void setChannels(Set<IrcChannel> channels) {
        this.channels = channels;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getServer() {
        return server;
    }

    public boolean isRegisterd() {
        return registerd;
    }

    public String getNick() {
        return nick;
    }

    public String getUser() {
        return user;
    }

    public String getRealName() {
        return realName;
    }

    public Date getLastPing() {
        return lastPing;
    }

    public void setLastPing(Date lastPing) {
        this.lastPing = lastPing;
    }

    public IrcConnection(IrcLineAnalyzer analyzer, String server, String nick) {
        this.uuid = UUID.randomUUID();
        this.server = server;
        this.nick = nick;
        this.analyzer = analyzer;
    }

    @Override
    public void run() {
        log.info("Connecting to server " + server);
        connect();

        if(getConnectionState() != CONNECTED) {
            log.info("Could not connect to server");
            return;
        }

        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        if (!registerWriter())
            return;

        // Join channels if exists
        channels.forEach(channel -> {
            join(channel.getChannel(), channel.getPassword());
        });

        while (CONNECTED == getState()) {
            try {
                String line = buf.readLine();

                // TODO: remove
                System.out.println("< " + line);

                if (registerd == false) {
                    register();
                    registerd = true;
                }

                if (line.startsWith("PING")) {
                    setConnectionState(CONNECTED);
                    registerPing(new Date());
                }

                analyzer.analyzeAndHandle(server, line, writer);

            } catch (SocketException e) {
                setConnectionState(DISCONNECTED);
                log.warn(e);
                socket = null;
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!socket.isConnected()) {
                socket = null;
                setConnectionState(DISCONNECTED);
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerPing(Date lastPing) {
        setLastPing(lastPing);
    }

    private boolean registerWriter() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    private void register() {
        send(IrcCommandMessage.registerNick(nick));

        // TODO: wtf
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        send(IrcCommandMessage.registerUser(nick, mode, realName, user));
    }

    @Override
    public void disconnect() {
        log.info(String.format("Disconnecting bot %s from server %s", nick,
                server));
        send(IrcCommandMessage.disconnect("Birch adios! " + nick));
        setConnectionState(DISCONNECTED);
    }

    @Override
    public void connect() {
        try {
            socket = new Socket(server, 6667);
            is = socket.getInputStream();
        } catch (IOException e) {
           log.warn(e);
        }

        if(socket == null || is == null) {
            setConnectionState(DISCONNECTED);
            return;
        }

        if (socket.isConnected()) {
            setConnectionState(CONNECTED);
        } else {
            setConnectionState(DISCONNECTED);
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void send(String data) {
        log.info(MessageFormat.format("sending: {0}", data));
        writer.println(data);
    }

    @Override
    public void join(String channel, String password) {
        IrcChannel ircChannel = new IrcChannel(channel, password);
        send(IrcCommandMessage.joinChannel(ircChannel));
        channels.add(ircChannel);
    }

    @Override
    public boolean connectionTimedOut() {
        ReadableInstant now = DateTime.now();
        ReadableInstant lastPing = new DateTime(getLastPing());
        Seconds seconds = Seconds.secondsBetween(lastPing, now);

        System.out.println("TIMEOUT: (" + seconds.getSeconds() + " > " + CONNECTION_TIMEOUT + ")");
        return seconds.getSeconds() > CONNECTION_TIMEOUT;
    }

    @Override
    public void reconnect() {
        run();
        System.out.println("reconnecting");
    }

    @Override
    public IrcConnectionState getState() {
        return connectionState;
    }

    public void setConnectionState(IrcConnectionState connectionState) {
        this.connectionState = connectionState;
    }
}
