import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Command {

    // all words from current command from input
    private final String[] tokens;

    public Command(String[] tokens) {
        this.tokens = tokens;
    }

    public boolean handle() {
        if (tokens[0].equalsIgnoreCase("quit")) {
            return false;
        } else if (tokens[0].equalsIgnoreCase("help")) {
            return handleHelp();
        } else if (tokens[0].equalsIgnoreCase("whoAmI")) {
            return handleWhoAmI();
        } else if (tokens[0].equalsIgnoreCase("load")) {
            return handleLoad();
        } else if (tokens[0].equalsIgnoreCase("wallet")) {
            return handleWallet();
        } else if (tokens[0].equalsIgnoreCase("buy")) {
            return handleBuy();
        } else if (tokens[0].equalsIgnoreCase("sell")) {
            return handleSell();
        } else if (tokens[0].equalsIgnoreCase("total")) {
            return handleTotal();
        }

        System.out.println("command not recognised, please type again");
        return true;
    }

    // handlers for all commands except 'quit'
    private boolean handleHelp() {
        return true;
    }

    private boolean handleWhoAmI() {
        return true;
    }

    private boolean handleLoad() {
        return true;
    }

    private boolean handleWallet() {
        return true;
    }

    private boolean handleBuy() {
        return true;
    }

    private boolean handleSell() {
        return true;
    }

    private boolean handleTotal() {
        return true;
    }
}
