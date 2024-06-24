package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.Purplecraft.plugin;

public class Speed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("purplecraft.setspeed")) {
            sender.sendMessage(paris.PluginPrefix("You don't have permission to use /speed!"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(paris.PluginPrefix("Specify a speed or a player and a speed!"));
            return true;
        }

        double speed;
        try {
            speed = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(paris.PluginPrefix("Invalid speed value! Please enter a number."));
            return true;
        }

        if (speed > 10 || speed < 0) {
            sender.sendMessage(paris.PluginPrefix("Speed must be between 0 and 10."));
            return false;
        }

        Player targetPlayer = (Player) sender;

        if (args.length > 1) {
            targetPlayer = plugin.getServer().getPlayer(args[1]);
            if (targetPlayer == null) {
                sender.sendMessage(paris.PluginPrefix("Player not found!"));
                return true;
            }
        }

        // Set speed based on flying state
        if (targetPlayer.isFlying()) {
            targetPlayer.setFlySpeed((float) speed / 10);
            sender.sendMessage(paris.PluginPrefix("Fly speed is set to " + speed + "."));
        } else {
            sender.sendMessage(paris.PluginPrefix("Walk speed is set to " + speed + "."));
            targetPlayer.setWalkSpeed(((float) speed / 10));
        }

        return true;
    }
}
