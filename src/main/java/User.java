import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.List;

public class User {

    private final String username;
    private List<Coin> coins;
    // available funds in USD
    // you can buy only Bitcoin with those funds
    private double funds;
    private final BinanceApiRestClient client;


    public User(String username) {
        this.username = username;
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        this.client = factory.newRestClient();
    }

    public String getUsername() {
        return username;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public Coin getCoinBySymbol(String symbol) {
        for (Coin c : coins) {
            if (c.getSymbol().equals(symbol)) {
                return c;
            }
        }
        return null;
    }

    public double getFunds() {
        return funds;
    }

    public void addFunds(double money) {
        funds += money;
    }

    public boolean buyBitcoinWithFunds(double money) {
        if (money > funds) {
            return false;
        }
        funds -= money;

        TickerStatistics tickerStatistics = client.get24HrPriceStatistics("USDTBTC");
        double quantity = Double.parseDouble(tickerStatistics.getLastPrice());
        AddQuantityToCoin("BTC", quantity);
        return true;
    }

    public void AddQuantityToCoin(String symbol, double quantity) {
        Coin c = getCoinBySymbol(symbol);
        if (c == null) {
            coins.add(new Coin("symbol", quantity));
        }
        else {
            c.addQuantity(quantity);
        }
    }

    public boolean RemoveQuantityFromCoin(String symbol, double quantity) {
        Coin c = getCoinBySymbol(symbol);
        return c != null && c.removeQuantity(quantity);
    }

    public boolean transferBetweenCoins(String toRemoveSymbol, double toRemoveQuantity, String toAddSymbol) {
        TickerStatistics tickerStatistics = client.get24HrPriceStatistics(toRemoveSymbol + toAddSymbol);
        double rate = Double.parseDouble(tickerStatistics.getLastPrice());
        double toAddQuantity = toRemoveQuantity * rate;

        if (!RemoveQuantityFromCoin(toRemoveSymbol, toRemoveQuantity)) {
            return false;
        }

        AddQuantityToCoin(toAddSymbol, toAddQuantity);
        return true;
    }

}
