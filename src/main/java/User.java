import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class User {

    private final String username;
    private List<Coin> coins;
    // available funds in USD
    // you can buy only Bitcoin with those funds
    private double funds;
    private final Exchange exchange;


    public User(String username) {
        this.username = username;
        coins = new ArrayList<>();
        funds = 0;
        this.exchange = new Exchange();
    }

    public User(Path filePath) {
        username = "TODO";
        this.exchange = new Exchange();
        // todo
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

    public boolean removeFunds(double money) {
        if (money > funds) {
            return false;
        }
        funds -= money;
        return true;
    }

    public boolean sellBitcoinToFunds(double quantity) {
        Coin btc = getCoinBySymbol("BTC");
        if (btc == null) {
            return false;
        }

        if (!btc.removeQuantity(quantity)) {
            return false;
        }

        double rate = exchange.getRate("BTC", "USDT");
        if (rate < 0.0) {
            return false;
        }

        double money = rate * quantity;
        addFunds(money);
        return true;
    }

    public boolean buyBitcoinWithFunds(double money) {
        if (money > funds) {
            return false;
        }
        funds -= money;

        double rate = exchange.getRate("BTC", "USDT");
        if (rate < 0.0) {
            return false;
        }
        rate = 1.0 / rate;
        double quantity = rate * money;
        AddQuantityToCoin("BTC", quantity);
        return true;
    }

    public void AddQuantityToCoin(String symbol, double quantity) {
        Coin c = getCoinBySymbol(symbol);
        if (c == null) {
            coins.add(new Coin(symbol, quantity));
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
        double rate = exchange.getRate(toRemoveSymbol, toAddSymbol);
        if (rate < 0.0) {
            return false;
        }

        double toAddQuantity = toRemoveQuantity * rate;

        if (!RemoveQuantityFromCoin(toRemoveSymbol, toRemoveQuantity)) {
            return false;
        }

        AddQuantityToCoin(toAddSymbol, toAddQuantity);
        return true;
    }

}
