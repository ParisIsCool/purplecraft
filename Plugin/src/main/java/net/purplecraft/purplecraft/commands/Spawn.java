package net.purplecraft.purplecraft.commands;

import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.Purplecraft.plugin;

public class Spawn implements CommandExecutor {

    private static Location spawnLocation;

    public static void loadSpawnLocation() {
        spawnLocation = plugin.getConfig().getLocation("spawn");
        if (spawnLocation == null) {
            plugin.getLogger().info("Spawn location not found in config, setting default spawn.");
            spawnLocation = plugin.getServer().getWorlds().getFirst().getSpawnLocation();
        }
    }

    public static Location getSpawnLocation() {
        return spawnLocation;
    }

    public static void setSpawnLocation(Location location) {
        spawnLocation = location;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("purplecraft.spawn")) {
            sender.sendMessage(paris.PluginPrefix("You don't have permission to use /spawn!"));
            return false;
        }

        if (getSpawnLocation() == null) {
            sender.sendMessage(paris.PluginPrefix("Spawn location not set!"));
            return false;
        }

        Player player = (Player) sender;
        player.teleport(getSpawnLocation());
        sender.sendMessage(paris.PluginPrefix("Teleporting to spawn..."));

        return true;
    }
}
