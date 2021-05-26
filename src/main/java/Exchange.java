import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.ArrayList;
import java.util.List;

public class Exchange {
    private final BinanceApiRestClient client;
    private final List<String> symbols;

    public Exchange() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        this.client = factory.newRestClient();
        List<SymbolInfo> exchangeInfos = client.getExchangeInfo().getSymbols();
        symbols = new ArrayList<>();
        for (SymbolInfo symbolInfo : exchangeInfos) {
            symbols.add(symbolInfo.getSymbol());
        }
    }

    private boolean goodSymbol(String symbol) {
        return symbols.contains(symbol);
    }

    public void showSymbols() {
        System.out.println(symbols);
    }

    public double getRate(String first, String second) {
        if (!goodSymbol(first+second)) {
            System.out.println("No " + first + " / " + second + " rate in database");
            return -1.0;
        }
        TickerStatistics tickerStatistics = client.get24HrPriceStatistics(first + second);
        return Double.parseDouble(tickerStatistics.getLastPrice());
    }
}
