package birch.irc.feature.ping;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Before;
import org.junit.Test;

import birch.irc.domain.TriggerLine;
import birch.irc.feature.ping.PingFeature;

public class PingFeatureTest {

    private static final String PING = "PING 123456";
    private static final String PONG = "PONG 123456";
    private PingFeature pf;

    @Before
    public void setup() {
        pf = new PingFeature();
    }

    @Test
    public void testPing() {
        String result = pf.handle(null, PING);
        assertThat(result, is(PONG));
    }

    @Test
    public void testNotCorrectLine() {
        String rs = pf.handle(null, "FUBAR");
        assertThat(rs, is(nullValue()));
    }
    
    @Test
    public void shouldNotHandleTriggers() {
        String rs = pf.handle(null, new TriggerLine("trigger", null));
        assertThat(rs,  is(nullValue()));
    }
    
    @Test
    public void shouldHandleAllLines() {
        assertThat(pf.triggerOnAllLines(), is(true));
    }
    
    @Test
    public void shouldReturnEmptyTriggerListTest() {
        assertThat(pf.triggers(), is(notNullValue()));
        assertThat(pf.triggers().size(), is(0));
    }
}
