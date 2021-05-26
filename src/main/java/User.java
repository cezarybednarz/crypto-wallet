import java.util.List;

public class User {

    private String username;
    private List<Coin> coins;
    // available funds in USD
    // you can buy only Bitcoin with those funds
    private double funds;

    public User(String username) {
        this.username = username;
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
        // todo
    }

    public void buyBitcoinWithAllFunds() {
        funds = 0.0;
        // todo
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

    public boolean transferBetweenCoins(Coin toAdd, Coin toRemove) {
        // todo
    }

}
