package birch.irc.feature.stock;

import java.util.List;


public interface StockRepository {
    Stock getStock(String alias);
    Stock save(Stock stock);
    List<Stock> fetchStocks();
    void deleteStock(Stock stock);
    Boolean stockExists(String alias);
}
