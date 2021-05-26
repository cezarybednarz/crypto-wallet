import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Command {

    // all words from current command from input
    private final String[] tokens;
    private User user;

    public enum ReturnCode {
        QUIT, OK, USER_LOADED
    }

    public Command(String[] tokens, User user) {
        this.tokens = tokens;
        this.user = user;
    }

    public ReturnCode handle() {
        if (tokens[0].equalsIgnoreCase("quit")) {
            return ReturnCode.QUIT;
        } else if (tokens[0].equalsIgnoreCase("help")) {
            return handleHelp();
        } else if (tokens[0].equalsIgnoreCase("whoAmI")) {
            return handleWhoAmI();
        } else if (tokens[0].equalsIgnoreCase("load")) {
            return handleLoad();
        } else if (tokens[0].equalsIgnoreCase("save")) {
            return handleSave();
        } else if (tokens[0].equalsIgnoreCase("wallet")) {
            return handleWallet();
        } else if (tokens[0].equalsIgnoreCase("buy")) {
            return handleBuy();
        } else if (tokens[0].equalsIgnoreCase("sell")) {
            return handleSell();
        } else if (tokens[0].equalsIgnoreCase("total")) {
            return handleTotal();
        } else if (tokens[0].equalsIgnoreCase("transfer")) {
            return handleTransfer();
        }

        System.out.println("command not recognised, please type again");
        return ReturnCode.OK;
    }

    // used to get user data after 'load' command
    public User getUser() {
        return user;
    }


    // handlers for all commands except 'quit'
    private ReturnCode handleHelp() {
        System.out.println(String.join("\n",
                "Available commands:",
                "'help'    : show all available commands",
                "'whoAmI'  : show user data",
                " todo"));
        return ReturnCode.OK;
    }

    private ReturnCode handleWhoAmI() {
        return ReturnCode.OK;
    }

    private ReturnCode handleLoad() {
        return ReturnCode.USER_LOADED;
    }

    private ReturnCode handleSave() {
        return ReturnCode.USER_LOADED;
    }

    private ReturnCode handleWallet() {
        return ReturnCode.OK;
    }

    private ReturnCode handleBuy() {
        return ReturnCode.OK;
    }

    private ReturnCode handleSell() {
        return ReturnCode.OK;
    }

    private ReturnCode handleTotal() {
        return ReturnCode.OK;
    }

    private ReturnCode handleTransfer() { return ReturnCode.OK; }
}
