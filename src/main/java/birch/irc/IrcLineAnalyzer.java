package birch.irc;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import birch.irc.domain.TriggerLine;
import birch.irc.feature.Features;

@Component
public class IrcLineAnalyzer {
    Logger log = Logger.getLogger(IrcLineAnalyzer.class);

    private Features features;

    @Autowired
    public IrcLineAnalyzer(Features features) {
        this.features = features;
    }

    public void analyzeAndHandle(String line, PrintWriter writer) {
        IrcPrivMessage parsedLine = IrcPrivMessage.fromLine(line);
        List<String> responses = new ArrayList<String>();

        if (parsedLine != null && isTriggerLine(parsedLine.getMessage())) {
            TriggerLine triggerLine = TriggerLine.fromPrivMsg(parsedLine);
            log.info("Handling trigger " + triggerLine.getTrigger() + " line: "
                    + line);
            responses.addAll(features.handleLineByTrigger(triggerLine));
        } else {
            List<String> res = features.handleAllLines(line);
            if (res.size() > 0)
                responses.addAll(res);
        }

        responses.forEach(response -> writer.println(response));
    }

    public static boolean isTriggerLine(String line) {
        if (line.startsWith("!")) {
            return true;
        } else {
            return false;
        }
    }

}
