package birch.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.UUID;

import org.apache.log4j.Logger;

import birch.irc.domain.Connection;

public class IrcConnection implements Connection, Runnable {
    Logger log = Logger.getLogger(IrcConnection.class);
    private UUID uuid;
    private String server;
    private Socket socket;
    private InputStream is;
    private boolean connected;
    private boolean registerd;
    private String nick;
    private PrintWriter writer;
    private String user;
    private String realName;
    private Integer mode = 0;
    private IrcLineAnalyzer analyzer;

    public UUID getUuid() {
        return uuid;
    }

    public String getServer() {
        return server;
    }

    public boolean isConnected() {
        return connected;
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
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        if (!registerWriter())
            return;

        while (connected) {
            try {
                String line = buf.readLine();

                // TODO: remove
                System.out.println("< " + line);

                if (registerd == false) {
                    register();
                    registerd = true;
                }

                analyzer.analyzeAndHandle(server, line, writer);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket.isConnected() == false) {
                connected = false;
            }
        }
        
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        this.connected = false;
    }

    @Override
    public void connect() {
        try {
            socket = new Socket(server, 6667);
            is = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (socket.isConnected())
            connected = true;
        else
            connected = false;
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
}
