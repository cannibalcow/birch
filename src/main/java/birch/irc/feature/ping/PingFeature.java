package birch.irc.feature.ping;

import java.util.List;

import org.springframework.stereotype.Component;

import birch.irc.IrcCommandMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;

import com.google.common.collect.ImmutableList;

@Component
public class PingFeature implements BotFeature {

    @Override
    public List<String> triggers() {
        return ImmutableList.of();
    }

    @Override
    public boolean triggerOnAllLines() {
        return true;
    }

    @Override
    public String handle(String line) {
        if (isPing(line)) {
            return IrcCommandMessage.pongReply(line);
        } else {
            return null;
        }
    }

    @Override
    public String handle(TriggerLine triggerLine) {
        return null;
    }

    private boolean isPing(String line) {
        if (line.startsWith("PING")) {
            return true;
        } else {
            return false;
        }
    }
}
