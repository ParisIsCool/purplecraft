package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.economy.Account;
import net.purplecraft.purplecraft.Purplecraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import static net.purplecraft.purplecraft.economy.paris.HEADER_CHAR;
import static org.bukkit.Bukkit.getServer;

public class Eco implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length != 3 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(HEADER_CHAR + ChatColor.RED + "Usage: /eco give <player> <amount>");
            return true;
        }

        if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("vault.economy.give")) {
            sender.sendMessage(HEADER_CHAR + ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(HEADER_CHAR + ChatColor.RED + "Invalid amount.");
            return false;
        }

        if (!Objects.equals(args[1], "*") && !Objects.equals(args[1], "**")) {    // Specific player
            Player target = Purplecraft.plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(HEADER_CHAR + ChatColor.RED + "Player not found.");
                return false;
            }
            if (sender instanceof Player player) {
                // Use the player object here
                Purplecraft.economyHandler.AddMoney(target,amount,player);
            } else {
                // Handle the case where the sender is not a player (e.g., console)
                Purplecraft.economyHandler.AddMoney(target,amount,null);
            }
            return true;
        } else {
            if (Objects.equals(args[1], "*")) { // All online players
                Server server = getServer();
                Collection<? extends Player> onlinePlayers = server.getOnlinePlayers();

                for (Player player : onlinePlayers) {
                    UUID uuid = player.getUniqueId();
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                    Purplecraft.economyHandler.AddMoney(offlinePlayer, amount, player);
                }
                return true;
            } else { // All players
                for (Account account : Purplecraft.getAccountManager().accounts.values()) {
                    UUID uuid = account.getUuid();
                    OfflinePlayer dbPlayer = Bukkit.getOfflinePlayer(uuid);

                    if (sender instanceof Player player) {
                        Purplecraft.economyHandler.AddMoney(dbPlayer, amount, player);
                    } else {
                        Purplecraft.economyHandler.AddMoney(dbPlayer, amount, null);
                    }
                }
                return true;
            }
        }
    }
}
