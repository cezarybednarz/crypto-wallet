import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Command {

    // all words from current command from input
    private String[] tokens;

    public Command(String[] tokens) {
        this.tokens = tokens;
    }

    public boolean handle() {
        if (tokens[0].equalsIgnoreCase("quit")) {
            return false;
        } else if (tokens[0].equalsIgnoreCase("help")) {
            return handleHelp();
        }
        else if (tokens[0].equalsIgnoreCase("whoami")) {
            return handleWhoAmI();
        }

        System.out.println("command not recognised, please type again");
        return false;
    }

    // handlers for all commands except 'quit'
    private boolean handleHelp() {
        return true;
    }

    private boolean handleWhoAmI() {
        return true;
    }
}
