package birch.irc.feature.urlgrabber;

import birch.irc.IrcPrivMessage;
import birch.irc.domain.TriggerLine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Ignore
public class UrlGrabberTest {

    private static final String PRIVMSG_B = "PRIVMSG #b :http://www.bbb.com";
    private static final String PRIVMSG_A = "PRIVMSG #a :http://www.aaa.com";
    private static final String URL_LINE_A = ":heldt|laptop!heldt@i.love.debian.org PRIVMSG #a :kolla in http://www.aaa.com";
    private static final String URL_LINE_B = ":heldt|laptop!heldt@i.love.debian.org PRIVMSG #b :kolla in http://www.bbb.com";
    private static final String LAST_URL_LINE_A = ":heldt|laptop!heldt@i.love.debian.org PRIVMSG #a :!lasturl";
    private static final String LAST_URL_LINE_B = ":heldt|laptop!heldt@i.love.debian.org PRIVMSG #b :!lasturl";
    
    private UrlGrabber ug;

    @Before
    public void setup() {
    }

    @Test
    public void shouldGrabUrl() {
        ug.handle(null, URL_LINE_A);

        assertThat(ug.getUrls().size(), is(1));
    }
    
    @Test
    public void shoulReturnBUrl() {
        ug.handle(null, URL_LINE_B);
        ug.handle(null, URL_LINE_A);
        assertThat(ug.getUrls().size(), is(2));

        IrcPrivMessage priv = IrcPrivMessage.fromLine(LAST_URL_LINE_B);
        TriggerLine trigger = TriggerLine.fromPrivMsg(priv);
        String res = ug.handle(null, trigger);
        assertThat(res, is(PRIVMSG_B));
    }
    
    @Test
    public void shoulReturnAUrl() {
        ug.handle(null, URL_LINE_B);
        ug.handle(null, URL_LINE_A);
        assertThat(ug.getUrls().size(), is(2));

        IrcPrivMessage priv = IrcPrivMessage.fromLine(LAST_URL_LINE_A);
        TriggerLine trigger = TriggerLine.fromPrivMsg(priv);
        String res = ug.handle(null, trigger);
        assertThat(res, is(PRIVMSG_A));
    }
}
