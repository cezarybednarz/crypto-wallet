import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

public class App {
    public static void main(String[] args) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("API-KEY", "SECRET");
        BinanceApiRestClient client = factory.newRestClient();

        System.out.println("Hello world");

        long serverTime = client.getServerTime();
        System.out.println(serverTime);

        TickerStatistics tickerStatistics = client.get24HrPriceStatistics("NEOETH");
        System.out.println(tickerStatistics.getLastPrice());
    }
}
