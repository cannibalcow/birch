package birch.irc.feature.stock;

import birch.irc.IrcCommandMessage;
import birch.irc.domain.BotFeature;
import birch.irc.domain.TriggerLine;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockFeature implements BotFeature {
    private final AvanzaScaper avanza;
    private final StockRepository stockRepo;
    Logger log = Logger.getLogger(StockFeature.class);
    private static final String STOCKS = "stocks";
    private static final String STOCK_REQUEST = "stock";
    private static final String STOCK_REGISTER = "register_stock";
    private static final String STOCK_DELETE = "delete_stock";
    private List<String> triggers;

    @Autowired
    public StockFeature(StockRepository stockRepo) {
        this.avanza = new AvanzaScaper();
        this.stockRepo = stockRepo;
        this.triggers = Arrays.asList(STOCK_REQUEST, STOCK_REGISTER, STOCK_DELETE, STOCKS);
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

    @Override
    public String handle(String server, TriggerLine triggerLine) {
        String trigger = triggerLine.getTrigger();
        switch (trigger) {
            case STOCK_REQUEST:
                return stockInfo(triggerLine);
            case STOCK_REGISTER:
                return registerStock(triggerLine);
            case STOCK_DELETE:
                return deleteStock(triggerLine);
            case STOCKS:
                return stocks(triggerLine);
            default:
                return null;
        }
    }

    private String stocks(TriggerLine triggerLine) {
        String receiver = triggerLine.getIrcPrivMessage().getReceiver();
        List<Stock> stocks = stockRepo.fetchStocks();
        if(stocks == null || stocks.isEmpty()) {
            return null;
        }

        List<String> aliases = stocks.stream().map(Stock::getAlias).collect(Collectors.toList());

        return IrcCommandMessage.sendPrivMessage(receiver, String.join(", ", aliases));
    }

    private String deleteStock(TriggerLine triggerLine) {
        String alias = triggerLine.getMessageWithoutTrigger();
        String receiver = triggerLine.getIrcPrivMessage().getReceiver();

        if(stockRepo.stockExists(alias)) {
            Stock mongoStock = stockRepo.getStock(alias);
            stockRepo.deleteStock(mongoStock);
            return IrcCommandMessage.sendPrivMessage(receiver, String.format("stock alias '%s' deleted'", alias));
        } else {
            return IrcCommandMessage.sendPrivMessage(receiver, String.format("stock alias '%s' does not exist'", alias));
        }
    }

    private String help(String receiver) {
        return IrcCommandMessage.sendPrivMessage(receiver, "Triggers: !stock, !register_stock <url> <alias>, !delete_stock <alias>");
    }

    private String registerStock(TriggerLine triggerLine) {
        String msg = triggerLine.getIrcPrivMessage().getMessage();
        String receiver = triggerLine.getIrcPrivMessage().getReceiver();
        Stock stock = splitMessageToRegisterMap(triggerLine.getIrcPrivMessage().getMessage());

        if(stock == null) {
            return IrcCommandMessage.sendPrivMessage(receiver, "Could not register stock...");
        }

        if(stockRepo.stockExists(stock.getAlias())) {
            Stock mongoStock = stockRepo.getStock(stock.getAlias());
            mongoStock.setUrl(stock.getUrl());
            stockRepo.save(mongoStock);
            return IrcCommandMessage.sendPrivMessage(receiver, String.format("stock '%s' updated with new url", mongoStock.getAlias()));
        } else {
            stockRepo.save(stock);
            return IrcCommandMessage.sendPrivMessage(receiver, String.format("Added the stock url %s with alias '%s'", stock.getUrl(), stock.getAlias()));
        }
    }

    private String stockInfo(TriggerLine triggerLine) {
        String alias = triggerLine.getMessageWithoutTrigger();
        Stock stock = stockRepo.getStock(alias);

        if(stock == null) {
            return help(triggerLine.getIrcPrivMessage().getReceiver());
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
