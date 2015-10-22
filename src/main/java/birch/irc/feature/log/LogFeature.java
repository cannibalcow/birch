package birch.irc.feature.log;

import birch.irc.IrcPrivMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Profile("dblog")
public class LogFeature implements BotFeature {

    private IrcLogLineRepository logRepo;

    @Autowired
    public LogFeature(IrcLogLineRepository logRepository) {
        this.logRepo = logRepository;
    }

    @Override
    public List<String> triggers() {
        return ImmutableList.of();
    }

    @Override
    public boolean triggerOnAllLines() {
        return true;
    }

    @Override
    public String handle(String server, String line) {
        IrcPrivMessage priv = IrcPrivMessage.fromLine(line);

        if (priv == null) {
            return null;
        }


        IrcLogLine log = new IrcLogLine();
        log.setChannel(priv.getReceiver().replaceAll("#", ""));
        log.setMessage(priv.getMessage());
        log.setNick(priv.getFrom());
        log.setTimestamp(new Date());
        log.setServer(server);
        logRepo.save(log);

        return null;
    }

    @Override
    public String handle(String server, TriggerLine triggerLine) {
        return null;
    }

}
