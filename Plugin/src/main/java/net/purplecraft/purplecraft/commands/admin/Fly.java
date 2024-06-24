package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.Purplecraft.plugin;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("purplecraft.fly")) {
            sender.sendMessage(paris.PluginPrefix("You don't have permission to use /speed!"));
            return true;
        }

        Player targetPlayer = null;
        if (args.length > 0) {
            targetPlayer = plugin.getServer().getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(paris.PluginPrefix("Player not found!"));
                return true;
            }
        } else {
            if (sender instanceof Player) {
                targetPlayer = (Player) sender;
            } else {
                sender.sendMessage(paris.PluginPrefix("This command can only be used by players!"));
                return true;
            }
        }

        // Set allow fly on flying state
        if (targetPlayer.getAllowFlight()) {
            sender.sendMessage(paris.PluginPrefix("Fly disabled."));
            targetPlayer.setAllowFlight(false);
        } else {
            sender.sendMessage(paris.PluginPrefix("Fly enabled."));
            targetPlayer.setAllowFlight(true);
        }

        return true;
    }
}
