package birch.irc.feature.stock;

import birch.irc.IrcPrivMessage;
import birch.irc.domain.TriggerLine;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class StockFeatureTest {
    private final String REGISTER_SUCCES = "PRIVMSG #a :mkay.. I have registererdrd the stock url https://www.avanza.se/aktier/om-aktien.html/264113/wntresearch with alias wnt";
    private final String REGISER_FAIL = "PRIVMSG #a :stock already registeredeedd... duuhh";

    @Test
    @Ignore
    public void registerTest() {
        StockFeature sf = new StockFeature();
        IrcPrivMessage privMsg = new IrcPrivMessage("heldt", "heldt@0::1", "#a", "!stock_register https://www.avanza.se/aktier/om-aktien.html/264113/wntresearch wnt");
        TriggerLine t1 = new TriggerLine("register_stock", privMsg);

        IrcPrivMessage registerMessage2 = new IrcPrivMessage("heldt", "heldt@0::1", "#a", "!stock_register https://www.avanza.se/aktier/om-aktien.html/168091/danaos-corp dansk");
        TriggerLine t2 = new TriggerLine("register_stock", registerMessage2);

        String result = sf.handle("server", t1);
        String result2 = sf.handle("server", t2);

//        Assert.assertEquals(result, REGISTER_SUCCES);
//        Assert.assertEquals(result2, REGISTER_SUCCES);
        Assert.assertEquals(sf.getStocks().size(), 2);
    }

    @Test
    @Ignore
    public void stockTest() {
        StockFeature sf = new StockFeature();
        IrcPrivMessage registerMessage = new IrcPrivMessage("heldt", "heldt@0::1", "#a", "!stock_register https://www.avanza.se/aktier/om-aktien.html/5479/teliasonera wnt");
        TriggerLine register = new TriggerLine("register_stock", registerMessage);

        IrcPrivMessage registerMessage2 = new IrcPrivMessage("heldt", "heldt@0::1", "#a", "!stock_register https://www.avanza.se/aktier/om-aktien.html/168091/danaos-corp dansk");
        TriggerLine register2 = new TriggerLine("register_stock", registerMessage);

        IrcPrivMessage stockMessage = new IrcPrivMessage("heldt", "heldt@0::1", "#a", "!stock wnt");
        TriggerLine stockLine = new TriggerLine("stock", stockMessage);

        IrcPrivMessage stockMessage2 = new IrcPrivMessage("heldt", "heldt@0::1", "#a", "!stock dansk");
        TriggerLine stockLine2 = new TriggerLine("stock", stockMessage);

        System.out.println(sf.handle("server", register));
        System.out.println(sf.handle("server", register2));

        System.out.println(sf.handle("server", stockLine));
        System.out.println(sf.handle("server", stockLine2));

    }

}
