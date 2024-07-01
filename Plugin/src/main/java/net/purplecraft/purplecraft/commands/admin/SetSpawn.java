package net.purplecraft.purplecraft.commands.admin;

import net.purplecraft.purplecraft.Purplecraft;
import net.purplecraft.purplecraft.commands.Spawn;
import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import static net.purplecraft.purplecraft.Purplecraft.plugin;
import static org.bukkit.Bukkit.getLogger;

public class SetSpawn implements CommandExecutor, Listener {

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!Purplecraft.getAccountManager().hasAccount(player)) { // FIRST TIME PLAYER JOIN
            getLogger().info("Setting first time player at spawn");
            player.teleport(Spawn.getSpawnLocation());
        }
    }

}
