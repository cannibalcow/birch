package birch.irc.feature.stock;

import birch.BirchApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BirchApplication.class)
public class MongoStockRepositoryTest {

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private MongoOperations mongo;

    private Stock testStock;

    @Before
    public void setup() {
        mongo.dropCollection(Stock.class);
        testStock = new Stock();
        testStock.setAlias("alias");
        testStock.setUrl("url");
    }


    @Test
    public void saveTest() {
        Stock s = stockRepo.save(testStock);
        Stock ss = stockRepo.getStock(s.getAlias());

        assertThat(ss, is(s));
    }

    @Test
    public void updateTest() {
        Stock s = stockRepo.save(testStock);
        Stock ss = stockRepo.getStock(s.getAlias());

        ss.setUrl("newurl");
        stockRepo.save(ss);

        Stock result = stockRepo.getStock("alias");

        assertThat(result, is(notNullValue()));
        assertThat(result.getAlias(), is("alias"));
        assertThat(result.getUrl(), is("newurl"));
    }

    @Test
    public void deleteTest() {
        Stock s = stockRepo.save(testStock);
        if(stockRepo.stockExists("alias"))
            stockRepo.deleteStock(testStock);
    }


}
