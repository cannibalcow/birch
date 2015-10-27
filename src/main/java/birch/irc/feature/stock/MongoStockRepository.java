package birch.irc.feature.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by heldt on 2015-10-27.
 */
@Repository
public class MongoStockRepository implements StockRepository {

    private final MongoOperations mongo;

    @Autowired
    public MongoStockRepository(MongoOperations operations) {
        this.mongo = operations;
    }

    @Override
    public Stock getStock(String alias) {
        return mongo.findOne(query(where("alias").is(alias)), Stock.class);
    }

    @Override
    public Stock save(Stock stock) {
        mongo.save(stock);
        return stock;
    }

    @Override
    public List<Stock> fetchStocks() {
        return mongo.findAll(Stock.class);
    }

    @Override
    public void deleteStock(Stock stock) {
        mongo.remove(stock);
    }

    @Override
    public Boolean stockExists(String alias) {
        Stock result = mongo.findOne(query(where("alias").is(alias)).limit(1), Stock.class);
        return result != null;
    }
}
