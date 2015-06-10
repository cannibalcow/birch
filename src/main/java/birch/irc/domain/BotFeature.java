package birch.irc.domain;

import java.util.List;

public interface BotFeature {
    List<String> triggers();
    boolean triggerOnAllLines();
    String handle(String server, String line);
    String handle(String server, TriggerLine triggerLine);
}
