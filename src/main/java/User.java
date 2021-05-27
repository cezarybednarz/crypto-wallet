import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {

    private String username;
    private List<Coin> coins;
    // available funds in USD
    // you can buy only Bitcoin with those funds
    private double funds;
    private final Exchange exchange;

    public User() {
        this.username = "";
        coins = new ArrayList<>();
        funds = 0;
        this.exchange = new Exchange();
    }

    public User(String username) {
        this.username = username;
        coins = new ArrayList<>();
        funds = 0;
        this.exchange = new Exchange();
    }

    public boolean LoadUserFromFile(String path) {
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            this.username = scanner.nextLine();
            this.funds = Double.parseDouble(scanner.nextLine());
            this.coins = new ArrayList<>();
            String[] coinList = scanner.nextLine().split(";");
            for (int i = 0; i < coinList.length; i += 2) {
                coins.add(new Coin(coinList[i], Double.parseDouble(coinList[i + 1])));
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean SaveUserToFile(String path) {
        File f = new File(path);
        try {
            if (f.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File already exists");
            }
            FileWriter writer = new FileWriter(path);
            writer.write(this.username + "\n");
            writer.write(this.funds + "\n");
            for (Coin c : coins) {
                writer.write(c.getSymbol() + ";" + c.getQuantity() + ";");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error, cannot create new file");
            return false;
        }
        return true;
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
        if (c == null) {
            return false;
        }

        // remove coin when quantity near 0.0
        if (Math.abs(quantity - c.getQuantity()) <= 1e-9) {
            coins.remove(c);
        }

        return c.removeQuantity(quantity);
    }
}
