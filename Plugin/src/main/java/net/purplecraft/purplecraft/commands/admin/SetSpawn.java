package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.commands.Spawn;
import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.Purplecraft.plugin;

public class SetSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("purplecraft.setspawn")) {
            sender.sendMessage(paris.PluginPrefix("You don't have permission to use /setspawn!"));
            return false;
        }

        if (sender instanceof Player) {
            Location location = ((Player) sender).getLocation();
            Spawn.setSpawnLocation(location);
            plugin.getConfig().set("spawn", location);
            plugin.saveConfig();

            sender.sendMessage(paris.PluginPrefix("Spawn location set!"));
            return true;
        } else {
            sender.sendMessage(paris.PluginPrefix("This command can only be used by players!"));
            return false;
        }
    }
}
