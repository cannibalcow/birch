package birch.controller.rest;

import birch.irc.feature.log.IrcLogLine;
import birch.irc.feature.log.IrcLogLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LogController {

    private IrcLogLineRepository logRepo;

    @Autowired
    public LogController(IrcLogLineRepository ircLogLineRepository) {
        this.logRepo = ircLogLineRepository;
    }

    @RequestMapping("/log/{server}/{channel}")
    public List<IrcLogLine> logs(@PathVariable String server, @PathVariable String channel) {
        return logRepo.findByIrcLogServerAndChannel(server, channel);
    }

}
