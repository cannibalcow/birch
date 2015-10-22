package birch.irc.feature.stock;

/**
 * Created by heldt on 2015-10-22.
 */
public class StockInfo {
    private String utvIdagPercent = "";
    private String utvIdagSEK = "";
    private String sell = "";
    private String buy = "";
    private String highestPrice = "";
    private String lowestPrice = "";
    private String totalVolume = "";
    private String updated = "";
    private String totalValueTraded;

    public void setUtvIdagPercent(String utvIdagPercent) {
        this.utvIdagPercent = utvIdagPercent;
    }

    public void setUtvIdagSEK(String utvIdagSEK) {
        this.utvIdagSEK = utvIdagSEK;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getFormatedString() {
        return String.format("%s %s Buy: %s Sell: %s Highest: %s Lowest: %s Volume Traded: %s %s Updated: %s", this.utvIdagPercent, this.utvIdagSEK, this.buy, this.sell, this.highestPrice, this.lowestPrice, this.totalVolume, this.totalValueTraded, this.updated);
    }

    public void setTotalValueTraded(String totalValueTraded) {
        this.totalValueTraded = totalValueTraded;
    }
}
