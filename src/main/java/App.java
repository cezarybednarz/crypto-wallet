import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    private final static String helloText = String.join("\n",
            "Welcome to Crypto Wallet!",
            "Use 'help' if you experience any problems :)");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(helloText);
        boolean finished = false;
        User user = null;

        while (!finished) {
            if (user == null) {
                System.out.print("> ");
            } else {
                System.out.print(user.getUsername() + "> ");
            }

            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            Command c = new Command(tokens, user);

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
        System.out.println("Exiting...");
    }
}
