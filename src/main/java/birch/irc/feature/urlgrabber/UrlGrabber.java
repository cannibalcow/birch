package birch.irc.feature.urlgrabber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import birch.irc.IrcMessage;
import birch.irc.IrcPrivMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;

@Component
public class UrlGrabber implements BotFeature {
    private static final String LASTURLS = "lasturls";
    Logger log = Logger.getLogger(UrlGrabber.class);
    private List<String> triggers;
    private List<Url> urls;

    public UrlGrabber() {
        triggers = new ArrayList<String>();
        urls = new ArrayList<Url>();
        triggers.add(LASTURLS);
    }

    @Override
    public List<String> triggers() {
        return triggers;
    }

    @Override
    public boolean triggerOnAllLines() {
        return true;
    }

    @Override
    public String handle(String line) {
        urls.addAll(getUrlsFromRow(line));
        return null;
    }

    public List<Url> getUrlsFromRow(String row) {
        Pattern pattern = Pattern
                .compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
                        + "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
                        + "|mil|biz|info|mobi|name|aero|jobs|museum"
                        + "|travel|[a-z]{2}))(:[\\d]{1,5})?"
                        + "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
                        + "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
                        + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
                        + "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
                        + "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
                        + "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        Matcher matcher = pattern.matcher(row);
        List<Url> result = new ArrayList<Url>();

        while (matcher.find()) {
            Url url = createMetaDataUrl(row);
            if (url == null)
                return result;

            url.setUrl(matcher.group());
            result.add(url);
        }
        return result;
    }

    private Url createMetaDataUrl(String row) {
        Pattern pattern = Pattern
                .compile(":(.+)\\!.+@(.+)\\ PRIVMSG (.+) :(.+)");
        Matcher matcher = pattern.matcher(row);
        if (!matcher.find())
            return null;

        Url url = new Url();
        url.setChannel(matcher.group(3));
        url.setServer(matcher.group(2));
        url.setNick(matcher.group(1));
        url.setDate(new Date());
        return url;
    }

    @Override
    public String handle(TriggerLine triggerLine) {
        if (LASTURLS.equals(triggerLine.getTrigger())) {
            IrcPrivMessage priv = triggerLine.getIrcPrivMessage();
            return IrcMessage.sendPrivMessage(priv.getReceiver(), urls.get(urls.size() - 1).getUrl());
        }
        return null;
    }
}
