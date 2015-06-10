package birch.irc.feature.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class IrcLogLine {
    
    @GeneratedValue
    @Id
    private Long id;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private IrcLogServer ircLogServer;
    
    @Column
    private String message;
    
    @Column
    private String channel;
    
    @Column 
    private String nick;
    
    @Column
    private Date timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IrcLogServer getIrcLogServer() {
        return ircLogServer;
    }

    public void setIrcLogServer(IrcLogServer ircLogServer) {
        this.ircLogServer = ircLogServer;
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
    public String toString() {
        return "IrcLogLine [id=" + id + ", ircLogServer=" + ircLogServer
                + ", message=" + message + ", channel=" + channel + ", nick="
                + nick + ", timestamp=" + timestamp + "]";
    }
}
