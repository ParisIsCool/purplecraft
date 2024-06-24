package net.purplecraft.purplecraft.economy;

import net.purplecraft.purplecraft.Purplecraft;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.ServicePriority;

import java.util.*;

import static net.purplecraft.purplecraft.economy.paris.*;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class EconomyHandler implements Listener {

    private final Purplecraft plugin;

    public EconomyHandler(Purplecraft plugin) {
        this.plugin = plugin;
    }

    public static boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found!");
            return false;
        }
        getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, Purplecraft.customEconomy, Purplecraft.plugin, ServicePriority.Highest);
        getLogger().info("Custom economy registered.");
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!Purplecraft.getAccountManager().hasAccount(player)) { // FIRST TIME PLAYER JOIN
            getLogger().info("Creating cash account...");
            Purplecraft.getAccountManager().createAccount(player);
        }
    }

    public void AddMoney(OfflinePlayer player, double amount, Player sender) {
        getLogger().info("Adding " + amount + " to " + player.getName());
        Purplecraft.getAccountManager().deposit(player, amount);
        if ( player.isOnline() ) {
            Player online_player = player.getPlayer();
            assert online_player != null;
            online_player.sendMessage(HEADER_CHAR + ChatColor.GREEN + "You have received " + ECONOMY_PREFIX + ChatColor.GOLD + amount + paris.ECONOMY_SUFFIX + ChatColor.GREEN + " from " + sender.getName());
        }
        sender.sendMessage(HEADER_CHAR + ChatColor.GREEN + "Gave " + ECONOMY_PREFIX + ChatColor.GOLD + amount + paris.ECONOMY_SUFFIX + ChatColor.GREEN + " to " + ChatColor.GOLD + player.getName());
    }

    public Player getPlayerByName(String name) {
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) { // Case-insensitive comparison
                return player;
            }
        }
        return null; // Player not found online
    }

    public String getBalanceTop() {
        String baltopPrefix = HEADER_CHAR + TEXT_COLOR + " Top Balances:\n§r"; // Replace HEADER_CHAR and TEXT_COLOR with actual values

        // Create a custom class to store player information for sorting
        class PlayerBalance implements Comparable<PlayerBalance> {
            private final UUID uuid;
            private final String name;
            private final double balance;

            public PlayerBalance(UUID uuid, String name, double balance) {
                this.uuid = uuid;
                this.name = name;
                this.balance = balance;
            }

            @Override
            public int compareTo(PlayerBalance other) {
                return Double.compare(other.balance, this.balance); // Descending order by balance
            }
        }

        // Convert accounts map entries to PlayerBalance objects and store them in a list
        List<PlayerBalance> playerBalances = new ArrayList<>();
        for (Account account : Purplecraft.getAccountManager().accounts.values()) {
            playerBalances.add(new PlayerBalance(account.getUuid(), account.getName(), account.getBalance()));
        }

        // Sort the list by balance in descending order
        Collections.sort(playerBalances);

        // Build the output string with top 10 entries
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10 && i < playerBalances.size(); i++) {
            PlayerBalance entry = playerBalances.get(i);
            sb.append(String.format(" %d. " + ECONOMY_PREFIX + "%.2f (%s)\n", i + 1, entry.balance, entry.name)); // Replace ECONOMY_PREFIX with actual value
        }

        return baltopPrefix + sb.toString().trim(); // Remove trailing newline (if any)
    }

}
