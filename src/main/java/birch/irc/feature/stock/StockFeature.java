package birch.irc.feature.stock;

import birch.irc.IrcCommandMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StockFeature implements BotFeature {
    private final AvanzaScaper avanza;
    Logger log = Logger.getLogger(StockFeature.class);
    private static final String STOCK_REQUEST = "stock";
    private static final String STOCK_REGISTER = "register_stock";
    private List<String> triggers;
    private Map<String, Stock> stocks = new HashMap<>();


    public StockFeature() {
        this.avanza = new AvanzaScaper();
        this.triggers = Arrays.asList(STOCK_REQUEST, STOCK_REGISTER);
    }

    @Override
    public List<String> triggers() {
        return triggers;
    }

    @Override
    public boolean triggerOnAllLines() {
        return false;
    }

    @Override
    public String handle(String server, String line) {
        return null;
    }

    public Map<String, Stock> getStocks() {
        return stocks;
    }

    @Override
    public String handle(String server, TriggerLine triggerLine) {
        if (STOCK_REQUEST.equals(triggerLine.getTrigger())) {
            return stockInfo(triggerLine);
        }

        if (STOCK_REGISTER.equals(triggerLine.getTrigger())) {
            return registerStock(triggerLine);
        }
        return null;
    }

    private String registerStock(TriggerLine triggerLine) {
        String msg = triggerLine.getIrcPrivMessage().getMessage();
        String receiver = triggerLine.getIrcPrivMessage().getReceiver();
        Stock stock = splitMessageToRegisterMap(triggerLine.getIrcPrivMessage().getMessage());

        if(stocks.containsKey(stock.getAlias())) {
            return IrcCommandMessage.sendPrivMessage(receiver, "stock already added... duuhh");
        } else {
            stocks.put(stock.getAlias(), stock);
            return IrcCommandMessage.sendPrivMessage(receiver, String.format("Added the stock url %s with alias '%s'", stock.getUrl(), stock.getAlias()));
        }
    }

    private String stockInfo(TriggerLine triggerLine) {
        String alias = triggerLine.getMessageWithoutTrigger();
        Stock stock;
        if(stocks.containsKey(alias)) {
            stock = stocks.get(alias);
        } else {
            return null;
        }

        String receiver = triggerLine.getIrcPrivMessage().getReceiver();

        StockInfo stockInfo = avanza.scrape(stock);

        if(stockInfo == null) {
            return null;
        }

        return IrcCommandMessage.sendPrivMessage(receiver, stockInfo.getFormatedString());
    }

    public Stock splitMessageToRegisterMap(String message) {
        Stock stock = new Stock();

        String[] split = message.split(" ");

        System.out.println(split.length);

        if (split.length != 3) {
            log.info("could not split url");
            return null;
        }
        stock.setUrl(split[1]);
        stock.setAlias(split[2]);

        return stock;
    }
}
