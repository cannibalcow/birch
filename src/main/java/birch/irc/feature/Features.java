package birch.irc.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;

@Component
public class Features {
    Logger log = Logger.getLogger(Features.class);

    @Autowired
    private List<BotFeature> features;

    public List<String> handleLineByTrigger(TriggerLine triggerLine) {
        List<String> responses = new ArrayList<String>();

        Stream<BotFeature> fs = features.stream().filter(
                f -> f.triggers().contains(triggerLine.getTrigger()));
        fs.forEach(feature -> {
            log.info("FEATURE IS HANDLING: " + feature.getClass());
            String response = feature.handle(triggerLine);

            if (response != null) {
                responses.add(response);
            }
        });

        return responses;
    }

    public List<String> handleAllLines(String line) {
        List<String> responses = new ArrayList<String>();

        features.stream().filter(f -> f.triggerOnAllLines() == true)
                .forEach(feature -> {
                    String response = feature.handle(line);
                    if (response != null)
                        responses.add(response);
                });

        return responses;
    }
}
