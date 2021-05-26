import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

public class Coin {

    private final BinanceApiRestClient client;
    private final String symbol;
    private double quantity;

    public Coin(String symbol, double quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        this.client = factory.newRestClient();
    }

    public double getPriceUSD() {
        TickerStatistics tickerStatistics = client.get24HrPriceStatistics(this.symbol + "USDT");
        return Double.parseDouble(tickerStatistics.getLastPrice());
    }

    public String getSymbol() {
        return symbol;
    }

    public double getQuantity() {
        return quantity;
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
