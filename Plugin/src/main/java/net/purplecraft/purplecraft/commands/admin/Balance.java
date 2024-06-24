package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.Purplecraft;
import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


import static net.purplecraft.purplecraft.economy.paris.ECONOMY_PREFIX;

public class Balance implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            double balance = Purplecraft.getAccountManager().getBalance(player);
            player.sendMessage(paris.HEADER_CHAR + ChatColor.GREEN + "Your balance is: " + ECONOMY_PREFIX + ChatColor.GOLD + balance + paris.ECONOMY_SUFFIX);
        } else {
            sender.sendMessage(paris.HEADER_CHAR + ChatColor.RED + "This command can only be used by players.");
        }
        return true;
    }
}
