package net.purplecraft.purplecraft.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.purplecraft.purplecraft.economy.paris.STARTING_BUX;

public class AccountManager {

    public final Map<UUID, Account> accounts = new HashMap<>();
    private final String saveFilePath = "plugins/Purplecraft/accounts.data";

    public boolean hasAccount(OfflinePlayer player) {
        return accounts.containsKey(player.getUniqueId());
    }

    public boolean createAccount(OfflinePlayer player) {
        if (hasAccount(player)) {
            return false;
        }
        accounts.put(player.getUniqueId(), new Account(player.getUniqueId(), player.getName(), STARTING_BUX));
        return true;
    }

    public double getBalance(OfflinePlayer player) {
        return accounts.getOrDefault(player.getUniqueId(), new Account(player.getUniqueId(), player.getName(), 0.0)).getBalance();
    }

    public boolean withdraw(OfflinePlayer player, double amount) {
        Account account = accounts.get(player.getUniqueId());
        if (account == null || account.getBalance() < amount) {
            return false;
        }
        account.setBalance(account.getBalance() - amount);
        try {
            saveBalances();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void deposit(OfflinePlayer player, double amount) {
        Account account = accounts.get(player.getUniqueId());
        if (account == null) {
            account = new Account(player.getUniqueId(), player.getName(), 0.0);
            accounts.put(player.getUniqueId(), account);
        }
        account.setBalance(account.getBalance() + amount);
        try {
            saveBalances();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBalances() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFilePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.out.println("Warning: Invalid format in accounts file: " + line);
                    continue;
                }

                UUID uuid = UUID.fromString(parts[0]);
                String name = parts[1];
                double balance = Double.parseDouble(parts[2]);

                accounts.put(uuid, new Account(uuid, name, balance));
            }
        }
    }

    public void saveBalances() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFilePath))) {
            for (Account account : accounts.values()) {
                bufferedWriter.write(account.getUuid().toString() + "," + account.getName() + "," + account.getBalance() + "\n");
            }
            bufferedWriter.flush();
        }
    }

    // New method to find players by username
    public OfflinePlayer findPlayerByUsername(String username) {
        // Check if the player is online
        Player onlinePlayer = Bukkit.getPlayerExact(username);
        if (onlinePlayer != null) {
            return onlinePlayer;
        }

        // If not online, check the stored accounts
        for (Account account : accounts.values()) {
            if (account.getName().equalsIgnoreCase(username)) {
                return Bukkit.getOfflinePlayer(account.getUuid());
            }
        }

        // As a fallback, use the deprecated method
        return Bukkit.getOfflinePlayer(username);
    }
}
