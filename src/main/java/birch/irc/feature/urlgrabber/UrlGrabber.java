package birch.irc.feature.urlgrabber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
    private UrlRepository repo;

    @Autowired
    public UrlGrabber(UrlRepository repository) {
        this.repo = repository;
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
    public String handle(String server, String line) {
        repo.save(getUrlsFromRow(line));
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
        url.setNick(priv.getFrom());
        url.setChannel(priv.getReceiver().replaceAll("#", ""));
        url.setDate(new Date());
        url.setMessage(priv.getMessage());
        return url;
    }

    @Override
    public String handle(String server, TriggerLine triggerLine) {
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
        PageRequest page = new PageRequest(0, 1, Direction.DESC, "date");

        String channel = priv.getReceiver().replaceAll("#", "");
        List<Url> url = repo.findLastUrlByChannel(channel, page);

        if (url != null && url.size() > 0) {
            return IrcCommandMessage.sendPrivMessage(priv.getReceiver(), url
                    .get(0).getUrl());
        } else {
            return null;
        }
    }
}
