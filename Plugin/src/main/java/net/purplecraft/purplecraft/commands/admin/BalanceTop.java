package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.Purplecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BalanceTop implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        sender.sendMessage(Purplecraft.economyHandler.getBalanceTop());
        return true;
    }
}
