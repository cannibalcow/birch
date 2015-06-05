package birch;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;

import birch.irc.IrcLineAnalyzer;

public class IrcLineAnalyzerTest {

    @Test
    public void isTriggerLine() {
        boolean res = IrcLineAnalyzer.isTriggerLine("!tweet it's a trigger");
        
        assertThat(res, Matchers.is(true));
    }
}
