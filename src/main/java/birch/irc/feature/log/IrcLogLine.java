package birch.irc.feature.log;

import java.util.Date;

public class IrcLogLine {

    private String server;

    private String message;

    private String channel;

    private String nick;

    private Date timestamp;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IrcLogLine that = (IrcLogLine) o;

        if (server != null ? !server.equals(that.server) : that.server != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (channel != null ? !channel.equals(that.channel) : that.channel != null) return false;
        if (nick != null ? !nick.equals(that.nick) : that.nick != null) return false;
        return !(timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null);

    }

    @Override
    public int hashCode() {
        int result = server != null ? server.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        result = 31 * result + (nick != null ? nick.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IrcLogLine{" +
                "server='" + server + '\'' +
                ", message='" + message + '\'' +
                ", channel='" + channel + '\'' +
                ", nick='" + nick + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
