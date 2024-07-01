package net.purplecraft.purplecraft.economy;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.purplecraft.purplecraft.Purplecraft;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class CustomEconomy extends AbstractEconomy {

    private final Purplecraft plugin;

    public CustomEconomy(Purplecraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "CustomEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f", amount);
    }

    @Override
    public String currencyNamePlural() {
        return "Coins";
    }

    @Override
    public String currencyNameSingular() {
        return "Coin";
    }

    public OfflinePlayer handleFindPlayer(String playerNameOrUUID) {
        try {
            // Attempt to parse the input as a UUID
            UUID playerUUID = UUID.fromString(playerNameOrUUID);
            // Get OfflinePlayer from the UUID
            return Bukkit.getOfflinePlayer(playerUUID);
        } catch (IllegalArgumentException e) {
            // Input is not a valid UUID, treat it as a player name
            @NotNull
            OfflinePlayer player = Purplecraft.getAccountManager().findPlayerByUsername(playerNameOrUUID);
            return player;
        }
    }


    @Override
    public boolean hasAccount(String s) {
        OfflinePlayer player = handleFindPlayer(s);
        return Purplecraft.getAccountManager().hasAccount(player);
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        OfflinePlayer player = handleFindPlayer(s);
        return Purplecraft.getAccountManager().hasAccount(player);
    }


    @Override
    public double getBalance(String s) {
        OfflinePlayer player = handleFindPlayer(s);
        return Purplecraft.getAccountManager().getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return Purplecraft.getAccountManager().getBalance(player);
    }

    @Override
    public double getBalance(String s, String s1) {
        OfflinePlayer player = handleFindPlayer(s);
        return Purplecraft.getAccountManager().getBalance(player);
    }

    @Override
    public boolean has(String s, double v) {
        double balance = getBalance(s);
        return (balance >= v);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        double balance = getBalance(s);
        return (balance >= v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return getEconomyResponse(amount, player);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        OfflinePlayer player = handleFindPlayer(s);
        return getEconomyResponse(v, player);
    }

    @NotNull
    private EconomyResponse getEconomyResponse(double v, OfflinePlayer player) {
        if (v < 0) {
            return new EconomyResponse(0, Purplecraft.getAccountManager().getBalance(player), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative amount");
        }

        if (Purplecraft.getAccountManager().withdraw(player, v)) {
            return new EconomyResponse(v, Purplecraft.getAccountManager().getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
        } else {
            return new EconomyResponse(0, Purplecraft.getAccountManager().getBalance(player), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        OfflinePlayer player = handleFindPlayer(s);
        return getEconomyResponse(player, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return getEconomyResponse(player, amount);
    }

    @NotNull
    private EconomyResponse getEconomyResponse(OfflinePlayer player, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, Purplecraft.getAccountManager().getBalance(player), EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative amount");
        }

        Purplecraft.getAccountManager().deposit(player, amount);
        return new EconomyResponse(amount, Purplecraft.getAccountManager().getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        OfflinePlayer player = handleFindPlayer(s);
        return getEconomyResponse(player, v);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    // Remaining methods can throw UnsupportedOperationException if not used or return default values
    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public List<String> getBanks() {
        throw new UnsupportedOperationException("Bank support not implemented");
    }

    @Override
    public boolean createPlayerAccount(String s) {
        OfflinePlayer player = handleFindPlayer(s);
        return Purplecraft.getAccountManager().createAccount(player);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return Purplecraft.getAccountManager().createAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }
}
