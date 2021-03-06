package birch.irc.feature.twitter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;

import birch.irc.IrcCommandMessage;
import birch.irc.IrcPrivMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;

@Component
@Profile("twitter")
public class TwitterFeature implements BotFeature{
    private static final String TWEET = "tweet";
    private Twitter twitter;
    private List<String> triggers;
       
    
    @Autowired
    public TwitterFeature(Twitter twitter) {
        this.twitter = twitter;
        this.triggers = new ArrayList<String>();
        triggers.add(TWEET);
    }
    
    @Override
    public List<String> triggers() {
        return triggers;
    }

    @Override
    public boolean triggerOnAllLines() {
        return false;
    }

    @Override
    public String handle(String server, String line) {
        return null;
    }

    @Override
    public String handle(String server, TriggerLine triggerLine) {
        if(TWEET.equals(triggerLine.getTrigger())) {
            IrcPrivMessage priv = triggerLine.getIrcPrivMessage();
            String tweet = triggerLine.getMessageWithoutTrigger();
            if(tweet.length() > 140) {
                return IrcCommandMessage.sendPrivMessage(priv.getReceiver(), "tweet to long, birch");
            } else {
                twitter.timelineOperations().updateStatus(tweet);
                return IrcCommandMessage.sendPrivMessage(priv.getReceiver(), "tweeting " + tweet);
            }
        }
        return null;
    }

}
