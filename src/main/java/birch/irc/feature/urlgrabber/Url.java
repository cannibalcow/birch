package birch.irc.feature.urlgrabber;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

public class Url implements Serializable {
    private static final long serialVersionUID = -4245757507490003947L;

    @Id
    private String id;

    private String nick;

    private String url;

    private String channel;

    private Date date;

    private String message;

    private String server;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "Url [id=" + id + ", nick=" + nick + ", url=" + url
                + ", channel=" + channel + ", date=" + date + ", message="
                + message + ", server=" + server + "]";
    }
}
