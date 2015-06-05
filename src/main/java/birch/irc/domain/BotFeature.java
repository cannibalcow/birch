package birch.irc.domain;

import java.util.List;

public interface BotFeature {
    List<String> triggers();
    boolean triggerOnAllLines();
    String handle(String line);
    String handle(TriggerLine triggerLine);
}
