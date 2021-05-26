import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Command {

    // all words from current command from input
    private final String[] tokens;
    private User user;
    private final Exchange exchange;

    public enum ReturnCode {
        QUIT, OK, ERROR, USER_LOADED
    }

    public Command(String[] tokens, User user, Exchange exchange) {
        this.tokens = tokens;
        this.user = user;
        this.exchange = exchange;
    }

    public ReturnCode handle() {
        if(tokens.length == 0) {
            return ReturnCode.OK;
        }

        if (tokens[0].equalsIgnoreCase("quit")) {
            return ReturnCode.QUIT;
        } else if (tokens[0].equalsIgnoreCase("help")) {
            return handleHelp();
        } else if (tokens[0].equalsIgnoreCase("whoAmI")) {
            return handleWhoAmI();
        } else if (tokens[0].equalsIgnoreCase("load")) {
            return handleLoad();
        } else if (tokens[0].equalsIgnoreCase("create")) {
            return handleCreate();
        } else if (tokens[0].equalsIgnoreCase("save")) {
            return handleSave();
        } else if (tokens[0].equalsIgnoreCase("wallet")) {
            return handleWallet();
        } else if (tokens[0].equalsIgnoreCase("buy")) {
            return handleBuy();
        } else if (tokens[0].equalsIgnoreCase("total")) {
            return handleTotal();
        } else if (tokens[0].equalsIgnoreCase("transfer")) {
            return handleTransfer();
        } else if (tokens[0].equalsIgnoreCase("rate")) {
            return handleRate();
        } else if (tokens[0].equalsIgnoreCase("symbols")) {
            return handleSymbols();
        } else if (tokens[0].equalsIgnoreCase("funds")) {
            return handleFunds();
        }

        System.out.println("command not recognised, please type again");
        return ReturnCode.OK;
    }

    // used to get user data after 'load' and 'create' command
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
        System.out.println("Username:        " + user.getUsername());
        System.out.println("Number of coins: " + user.getCoins().size());
        System.out.println("Funds:           " + String.format("%.2f", user.getFunds()) + "$");

        return ReturnCode.OK;
    }

    private ReturnCode handleLoad() {
        user = new User();
        if (tokens.length == 1) {
            System.out.println("Please enter path name");
            return ReturnCode.ERROR;
        }
        if (!user.LoadUserFromFile(tokens[1])) {
            return ReturnCode.ERROR;
        }
        return ReturnCode.USER_LOADED;
    }

    private ReturnCode handleCreate() {
        user = new User();
        if (tokens.length == 1) {
            System.out.println("Please specify username");
            return ReturnCode.ERROR;
        }
        String username = tokens[1];
        this.user = new User(username);
        return ReturnCode.USER_LOADED;
    }

    private ReturnCode handleSave() {
        if (tokens.length == 1) {
            System.out.println("Please enter path name");
            return ReturnCode.ERROR;
        }
        if (!user.SaveUserToFile(tokens[1])) {
            return ReturnCode.ERROR;
        }
        return ReturnCode.OK;
    }

    private ReturnCode handleWallet() {
        if (user == null) {
            System.out.println("Please load user first");
            return ReturnCode.ERROR;
        }
        String[][] quantitiesAndValue = new String[2][user.getCoins().size()];
        for (int i = 0; i < user.getCoins().size(); i++) {
            quantitiesAndValue[0][i] = String.format("%.9f", user.getCoins().get(i).getQuantity());
            quantitiesAndValue[1][i] = String.format("%.9f", user.getCoins().get(i).getPriceUSD());
        }
        List<String> symbols = new ArrayList<>();
        for (Coin c : user.getCoins()) {
            symbols.add(c.getSymbol());
        }
        Table showWallet = new Table(symbols, Arrays.asList("coin", "value in $"), quantitiesAndValue);
        System.out.println(showWallet);
        return ReturnCode.OK;
    }

    private ReturnCode handleBuy() {
        if (tokens.length != 5) {
            System.out.println("Usage: buy <symbol> <symbol> <flag> <value>");
            return ReturnCode.ERROR;
        }

        boolean isPercentage;
        if (tokens[3].equalsIgnoreCase("-p")) {
            isPercentage = true;
        } else if (tokens[3].equalsIgnoreCase("-q")) {
            isPercentage = false;
        } else {
            System.out.println("Unknown flag");
            return ReturnCode.ERROR;
        }

        Coin coinToSell = user.getCoinBySymbol(tokens[2]);
        if (coinToSell == null) {
            System.out.println("There is no such coin as " + tokens[2] + " in your wallet");
            return ReturnCode.ERROR;
        }

        double value = Double.parseDouble(tokens[4]);

        double quantityToSell = isPercentage ? coinToSell.getQuantityByPercentage(value) : value;
        double rate = exchange.getRate(tokens[2], tokens[1]);
        double quantityToAdd = quantityToSell * rate;

        if (!user.RemoveQuantityFromCoin(tokens[2], quantityToSell)) {
            System.out.println("Not enough quantity of coin " + tokens[2] + " to sell");
        }

        user.AddQuantityToCoin(tokens[1], quantityToAdd);

        return ReturnCode.OK;
    }

    private ReturnCode handleTotal() {
        double total = 0.0;
        for (Coin c : user.getCoins()) {
            total += c.getPriceUSD();
        }
        System.out.println("Total account value: " + String.format("%.2f", total) + "$");
        return ReturnCode.OK;
    }

    private ReturnCode handleTransfer() {
        if(user == null) {
            System.out.println("Please load user first");
            return ReturnCode.ERROR;
        }
        if (tokens.length == 1) {
            System.out.println("Please specify options for transfer");
            return ReturnCode.ERROR;
        }

        if (tokens[1].equalsIgnoreCase("in")) {
            if (tokens.length == 2) {
                System.out.println("Please specify quantity of $ to transfer to your account");
                return ReturnCode.ERROR;
            }
            double money = Double.parseDouble(tokens[2]);
            user.addFunds(money);
            return ReturnCode.OK;
        } else if (tokens[1].equalsIgnoreCase("out")) {
            if (tokens.length == 2) {
                System.out.println("Please specify quantity of $ to transfer to your account");
                return ReturnCode.ERROR;
            }
            double money = Double.parseDouble(tokens[2]);
            if (!user.removeFunds(money)) {
                System.out.println("Not enough funds");
                return ReturnCode.ERROR;
            }
            return ReturnCode.OK;
        } else if (tokens[1].equalsIgnoreCase("btc")) {
            if (tokens.length < 4) {
                System.out.println("Please specify options for transferring BTC");
                return ReturnCode.ERROR;
            }

            boolean isPercentage;

            if (tokens[3].equalsIgnoreCase("-p")) {
                isPercentage = true;
            } else if (tokens[3].equalsIgnoreCase("-q")) {
                isPercentage = false;
            } else {
                System.out.println("Unknown flag for transferring BTC");
                return ReturnCode.ERROR;
            }

            double value = Double.parseDouble(tokens[4]);

            if (tokens[2].equalsIgnoreCase("out")) {
                if (user.getCoinBySymbol("BTC") == null) {
                    System.out.println("No BTC in wallet");
                }

                Coin btc = user.getCoinBySymbol("BTC");

                if (isPercentage) {
                    value = btc.getQuantityByPercentage(value);
                }

                if (!user.sellBitcoinToFunds(value)) {
                    System.out.println("Not enough BTC in account");
                    return ReturnCode.ERROR;
                }
                return ReturnCode.OK;

            } else if (tokens[2].equalsIgnoreCase("in")) {
                if (isPercentage) {
                    value = user.getFunds() * value / 100;
                }

                if (!user.buyBitcoinWithFunds(value)) {
                    System.out.println("Not enough funds to buy BTC\n");
                    return ReturnCode.ERROR;
                }

                return ReturnCode.OK;
            }

            System.out.println("Unknown option for transferring BTC");
            return ReturnCode.ERROR;
        }

        System.out.println("Unknown option for 'transfer' command");
        return ReturnCode.ERROR;
    }

    private ReturnCode handleRate() {
        if (tokens.length < 3) {
            System.out.println("Please specify coin names to check the rate");
            return ReturnCode.ERROR;
        }
        double rate = exchange.getRate(tokens[1], tokens[2]);
        if (rate < 0.0) {
            return ReturnCode.ERROR;
        }
        System.out.println("Rate of " + tokens[1] + "/" + tokens[2] + ": " + rate);
        return ReturnCode.OK;
    }

    private ReturnCode handleSymbols() {
        exchange.showSymbols();
        return ReturnCode.OK;
    }

    private ReturnCode handleFunds() {
        if (user == null) {
            System.out.println("Please load user first");
            return ReturnCode.ERROR;
        }
        System.out.println("Funds: " + user.getFunds());
        return ReturnCode.OK;
    }
}
