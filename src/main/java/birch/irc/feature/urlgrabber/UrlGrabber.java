package birch.irc.feature.urlgrabber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import birch.irc.IrcCommandMessage;
import birch.irc.IrcPrivMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;

@Component
public class UrlGrabber implements BotFeature {
    private static final String LASTURL = "lasturl";
    Logger log = Logger.getLogger(UrlGrabber.class);
    private List<String> triggers;
    private List<Url> urls;

    public UrlGrabber() {
        triggers = new ArrayList<String>();
        urls = new ArrayList<Url>();
        triggers.add(LASTURL);
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

    public List<Url> getUrls() {
        return urls;
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
            log.info("Adding url: " + url.getUrl());
        }
        return result;
    }

    private Url createMetaDataUrl(String line) {
        IrcPrivMessage priv = IrcPrivMessage.fromLine(line);

        Url url = new Url();
        url.setFrom(priv.getFrom());
        url.setTo(priv.getReceiver());
        url.setDate(new Date());
        return url;
    }

    @Override
    public String handle(TriggerLine triggerLine) {
        switch (triggerLine.getTrigger()) {
        case LASTURL:
            return lastUrl(triggerLine);
        default:
            return null;
        }
    }

    /**
     * Return last posted url in channel
     * 
     * @param triggerLine
     * @return
     */
    private String lastUrl(TriggerLine triggerLine) {
        IrcPrivMessage priv = triggerLine.getIrcPrivMessage();

        System.out.println(priv.getFrom());
        Url url = urls.stream()
                .filter(u -> u.getTo().equals(priv.getReceiver()))
                .reduce((u1, u2) -> u1.getDate().after(u2.getDate()) ? u1 : u2).get();

        // User res = users.stream().filter(user ->
        // user.name.equals("apa")).reduce((u, t) -> u.date.after(t.date) ? u :
        // t).get();
        return IrcCommandMessage.sendPrivMessage(priv.getReceiver(), url.getUrl());
    }
}
