import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {

    private final static String helloText = String.join("\n",
            "Welcome to Crypto Wallet!",
            "Use 'help' if you experience any problems");

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
        test();

        Scanner scanner = new Scanner(System.in);
        System.out.println(helloText);
        boolean finished = false;

        String currentUser = "";
        while (!finished) {
            System.out.print(currentUser + "> ");

            String line = scanner.nextLine();
            String[] tokens = line.split(" ");
            Command c = new Command(tokens);

            if (!c.handle()) {
               finished = true;
            }
        }
        System.out.println("Exiting...");
    }
}
