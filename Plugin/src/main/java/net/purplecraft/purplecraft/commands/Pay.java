package net.purplecraft.purplecraft.commands;

import net.purplecraft.purplecraft.Purplecraft;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.economy.paris.HEADER_CHAR;

public class Pay implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(HEADER_CHAR + ChatColor.RED + "Usage: /pay <player> <amount>");
            return true;
        }
        if (sender instanceof Player player) {
            Player target = Purplecraft.economyHandler.getPlayerByName(args[0]);
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(HEADER_CHAR + ChatColor.RED + "Invalid amount.");
                return false;
            }
            double sender_balance = Purplecraft.getAccountManager().getBalance(player);
            if (sender_balance < amount ) {
                sender.sendMessage(HEADER_CHAR + ChatColor.RED + "Insufficient funds.");
                return false;
            } else {
                Purplecraft.economyHandler.AddMoney(target,amount,player);
                Purplecraft.getAccountManager().withdraw(player, amount);
                return true;
            }
        } else {
            sender.sendMessage(HEADER_CHAR + ChatColor.RED + "This command can only be used by players.");
        }
        return true;
    }
}
