package birch.irc.domain;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import birch.irc.IrcPrivMessage;

public class TriggerLine {
    private String trigger;
    private IrcPrivMessage ircPrivMessage;

    public TriggerLine(String trigger, IrcPrivMessage privmsg) {
        this.ircPrivMessage = privmsg;
        this.trigger = trigger;
    }

    public String getTrigger() {
        return trigger;
    }

    public IrcPrivMessage getIrcPrivMessage() {
        return ircPrivMessage;
    }
    
    public String getMessageWithoutTrigger() {
        String str = ircPrivMessage.getMessage();
        return str.replaceAll(MessageFormat.format("^\\!{0} ", trigger), "");
    }

    public static TriggerLine fromPrivMsg(IrcPrivMessage privmsg) {
        Pattern pattern = Pattern.compile("^\\!(\\w+)");

        Matcher matcher = pattern.matcher(privmsg.getMessage());

        if (!matcher.find())
            return null;

        return new TriggerLine(matcher.group(1), privmsg);
    }
}
