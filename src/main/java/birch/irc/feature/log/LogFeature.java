package birch.irc.feature.log;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import birch.irc.IrcPrivMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;

import com.google.common.collect.ImmutableList;

@Component
@Profile("dblog")
public class LogFeature implements BotFeature {

    private IrcLogServerRepository serverRepo;
    private IrcLogRepository logRepo;

    @Autowired
    public LogFeature(IrcLogRepository logRepo,
            IrcLogServerRepository serverRepository) {
        this.logRepo = logRepo;
        this.serverRepo = serverRepository;
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
    @Transactional
    public String handle(String server, String line) {
        IrcPrivMessage priv = IrcPrivMessage.fromLine(line);

        if (priv == null) {
            return null;
        }

        IrcLogServer ircServer = serverRepo.findByServer(server);

        if (ircServer == null) {
            ircServer = new IrcLogServer();
            ircServer.setServer(server);
            serverRepo.save(ircServer);
        }

        IrcLogLine log = new IrcLogLine();
        log.setChannel(priv.getReceiver().replaceAll("#", ""));
        log.setIrcLogServer(ircServer);
        log.setMessage(priv.getMessage());
        log.setNick(priv.getFrom());
        log.setTimestamp(new Date());

        logRepo.save(log);

        return null;
    }

    @Override
    public String handle(String server, TriggerLine triggerLine) {
        return null;
    }

}
