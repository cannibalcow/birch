package birch.irc.feature.stock;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AvanzaScaper {
    Logger log = Logger.getLogger(AvanzaScaper.class);

    private static final String TOTAL_CURRENCY = "#surface > div:nth-child(4) > div.column.grid_5 > div:nth-child(2) > div > div > div.tRight.bold";
    private static final String TOTAL_VALUE_TRADED = "#surface > div:nth-child(4) > div.column.grid_5 > div:nth-child(2) > div > div > div.tRight.bold > span";
    private static final String TOTAL_VOLUME = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(9) > span.totalVolumeTraded.SText.bold";
    private static final String UTV_IDAG_SEK_PATH = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(3) > div > span.change.SText.bold.negative";
    private static final String UTV_IDAG_PERCENT_CSS_PATH = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(2) > div:nth-child(2) > span.changePercent.SText.bold.negative";
    private static final String UTV_IDAG_PERCENT_CSS_PATH_NEG = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(2) > div:nth-child(2) > span.changePercent.SText.bold.positive";
    private static final String BUY = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(4) > span.buyPrice.SText.bold";
    private static final String SELL = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(5) > span.sellPrice.SText.bold";
    private static final String HIGHEST_PRICE = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(7) > span.highestPrice.SText.bold";
    private static final String LOWEST_PRICE = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(8) > span.lowestPrice.SText.bold";
    private static final String UPDATED = "#surface > div:nth-child(2) > div > div > div > div > ul > li:nth-child(10) > span.updated.SText.bold";

    public StockInfo scrape(Stock stock) {
        try {
            if(stock.getUrl() == null) {
                log.error("No given");
                return null;
            }

            Document doc = Jsoup.connect(stock.getUrl()).get();
            StockInfo stockInfo = new StockInfo();
            setUtvIdagSEK(doc, stockInfo);
            setUtvIdagPercent(doc, stockInfo);
            setBuy(doc, stockInfo);
            setSell(doc, stockInfo);
            setHighestPrice(doc, stockInfo);
            setLowestPrice(doc, stockInfo);
            setTotalVolume(doc, stockInfo);
            setUptade(doc, stockInfo);
            setTotalValueTraded(doc, stockInfo);
            return stockInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setTotalValueTraded(Document doc, StockInfo stockInfo) {
        Elements cur = doc.select(TOTAL_CURRENCY);

        if (cur.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setTotalValueTraded(cur.get(0).text());
    }

    private void setUptade(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(UPDATED);

        if (f.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setUpdated(f.get(0).text());
    }

    private void setTotalVolume(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(TOTAL_VOLUME);

        if (f.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setTotalVolume(f.get(0).text());
    }

    private void setLowestPrice(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(LOWEST_PRICE);

        if (f.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setLowestPrice(f.get(0).text());
    }

    private void setHighestPrice(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(HIGHEST_PRICE);

        if (f.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setHighestPrice(f.get(0).text());
    }

    private void setSell(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(SELL);

        if (f.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setSell(f.get(0).text());
    }

    private void setBuy(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(BUY);

        if (f.size() == 0) {
            log.info("did not find anything");
            return;
        }

        stockInfo.setBuy(f.get(0).text());
    }

    private void setUtvIdagPercent(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(UTV_IDAG_PERCENT_CSS_PATH);
        Elements ff = doc.select(UTV_IDAG_PERCENT_CSS_PATH_NEG);

        if (f.size() > 0) {
            stockInfo.setUtvIdagPercent(f.get(0).text());
        } else if(ff.size() > 0) {
            stockInfo.setUtvIdagPercent(ff.get(0).text());
        }
    }

    private void setUtvIdagSEK(Document doc, StockInfo stockInfo) {
        Elements f = doc.select(UTV_IDAG_SEK_PATH);

        if (f.size() == 0) {
            return;
        }

        stockInfo.setUtvIdagSEK(f.get(0).text());
    }

}
