import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

public class Coin {

    private final String symbol;
    private double quantity;
    private final Exchange exchange;

    public Coin(String symbol, double quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.exchange = new Exchange();
    }

    public double getPriceUSD() {
        double rate = exchange.getRate(symbol, "USDT");
        if (rate < 0.0) {
            return rate;
        }
        return rate * quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getQuantityByPercentage(double p) {
        // add 1e-9 to fix rounding errors
        return quantity * p / (100.0 + 1e-9);
    }

    public void addQuantity(double q) {
        quantity += q;
    }

    public boolean removeQuantity(double q) {
        if(q > quantity) {
            return false;
        }
        quantity -= q;
        return true;
    }
}
