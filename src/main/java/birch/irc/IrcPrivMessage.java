package birch.irc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrcPrivMessage {
    private String receiver;
    private String message;
    private String from;
    private String hostname;

    public IrcPrivMessage(String from, String hostname, String receiver,
            String message) {
        this.from = from;
        this.hostname = hostname;
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public static IrcPrivMessage fromLine(String line) {
        Pattern pattern = Pattern.compile(":(.+)\\!(.+) PRIVMSG (.+) :(.+)");

        Matcher groups = pattern.matcher(line);

        if(groups.find()) {
            String from = groups.group(1);
            String hostname = groups.group(2);
            String receiver = groups.group(3);
            String message = groups.group(4);
            return new IrcPrivMessage(from, hostname, receiver, message);
        } else {
            return null;
        }
    }
}
