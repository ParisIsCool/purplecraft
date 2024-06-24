package net.purplecraft.purplecraft.commands;

import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.Purplecraft.strmotd;

public class MOTD implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("purplecraft.motd")) {
            sender.sendMessage(paris.PluginPrefix("You don't have permission to use /motd!"));
            return false;
        }

        sender.sendMessage(strmotd);

        return true;
    }
}