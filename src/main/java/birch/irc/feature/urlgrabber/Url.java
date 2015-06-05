package birch.irc.feature.urlgrabber;

import java.util.Date;

public class Url {

    protected Long id;
    protected String server;
    protected String channel;
    protected String url;
    protected Date date;
    protected String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Url [id=" + id + ", server=" + server + ", channel=" + channel
                + ", url=" + url + ", date=" + date + ", nick=" + nick + "]";
    }
}
