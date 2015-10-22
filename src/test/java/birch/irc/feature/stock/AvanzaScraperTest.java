package birch.irc.feature.stock;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

/**
 * Created by heldt on 2015-10-22.
 */
public class AvanzaScraperTest {

    @Ignore
    @Test
    public void scrapeTest() {
        AvanzaScaper as = new AvanzaScaper();

        Stock stock = new Stock();
        stock.setUrl("https://www.avanza.se/aktier/om-aktien.html/264113/wntresearch");
        stock.setAlias("wnt");
        StockInfo stockInfo = as.scrape(stock);
        System.out.println(stockInfo.getFormatedString());

    }
}
