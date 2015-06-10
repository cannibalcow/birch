package birch.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import birch.irc.feature.log.IrcLogLine;
import birch.irc.feature.log.IrcLogRepository;
import birch.irc.feature.log.IrcLogServer;
import birch.irc.feature.log.IrcLogServerRepository;

import com.google.common.collect.ImmutableList;

@RestController
public class LogController {

    private IrcLogRepository logRepo;
    private IrcLogServerRepository serverRepo;

    @Autowired
    public LogController(IrcLogRepository ircLogRepository, IrcLogServerRepository ircLogServerRepository) {
        this.logRepo = ircLogRepository;
        this.serverRepo = ircLogServerRepository;
    }
    
    @RequestMapping("/log/{server}/{channel}")
    public List<IrcLogLine> logs(@PathVariable String server, @PathVariable String channel) {
        IrcLogServer ircLogServer = serverRepo.findByServer(server);
    
           if(ircLogServer == null) {
               return ImmutableList.of();
           }
           
           return logRepo.findByIrcLogServerAndChannel(ircLogServer, channel);
    }
}
