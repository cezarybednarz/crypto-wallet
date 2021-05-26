import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        System.out.println(String.join("\n",
                "Welcome to Crypto Wallet!",
                "Use 'help' if you experience any problems :)"));

        Scanner scanner = new Scanner(System.in);
        boolean finished = false;
        User user = null;
        Exchange exchange = new Exchange();

        while (!finished) {
            if (user == null) {
                System.out.print("> ");
            } else {
                System.out.print(user.getUsername() + "> ");
            }

            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            Command c = new Command(tokens, user, exchange);

            switch (c.handle()) {
                case QUIT:
                    finished = true;
                    break;
                case USER_LOADED:
                    user = c.getUser();
                    break;
                case ERROR:
                    System.out.println("Error occurred!");
                    break;
            }
        }
        scanner.close();
        System.out.println("Exiting...");
    }
}
