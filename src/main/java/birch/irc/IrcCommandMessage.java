package birch.irc;

import java.text.MessageFormat;

public class IrcCommandMessage {

    private IrcCommandMessage() {
    }

    public static String registerNick(String nick) {
        return MessageFormat.format("NICK {0}", nick);
    }

    /**
     * Returns pong message, input "PING 123" outputs "PONG 123"
     * 
     * @param ping
     * @return
     */
    public static String pongReply(String ping) {
        return ping.replaceAll("^PING", "PONG");
    }

    /**
     * Register user
     * 
     * @param user
     * @param mode
     * @param unused
     * @param realName
     * @return
     */
    public static String registerUser(String user, Integer mode, String unused,
            String realName) {
        return String.format("USER %s %d %s :%s", user, mode, unused, realName);
    }

    public static String joinChannel(IrcChannel channel) {
        if (channel.getPassword() != null) {
            return String.format("JOIN #%s %s", channel.getChannel(), channel.getPassword());
        } else {
            return String.format("JOIN #%s", channel.getChannel());
        }
    }

    public static String sendPrivMessage(String receiver, String message) {
        return MessageFormat.format("PRIVMSG {0} :{1}", receiver, message);
    }

    public static String disconnect(String quitMessage) {
        return MessageFormat.format("QUIT {0}", quitMessage);
    }
}
