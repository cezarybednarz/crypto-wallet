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

    public static void test() {
        Integer[][] t = new Integer[2][3];
        t[0][0] = 1;
        t[0][1] = 2;
        t[0][2] = 333333333;
        t[1][0] = 4;
        t[1][1] = 5;
        t[1][2] = 6;
        Table table = new Table(Arrays.asList("a_coll", "b_colll"), Arrays.asList("a_row", "b_roww", "c_rowww"), t);
        System.out.println(table);
    }

    public static void main(String[] args) {
        //test();

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

            if (!c.handle()) {
               finished = true;
            }

            // retrieve user after 'load' command
            if (tokens[0].equalsIgnoreCase("load")) {
                user = c.getUser();
            }
        }
        System.out.println("Exiting...");
    }
}
