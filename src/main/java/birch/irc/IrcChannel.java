package birch.irc;

/**
 * Created by heldt on 2015-11-05.
 */
public class IrcChannel {
    private String channel;
    private String password;

    public IrcChannel(String channel, String password) {
        this.channel = channel;
        this.password = password;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
