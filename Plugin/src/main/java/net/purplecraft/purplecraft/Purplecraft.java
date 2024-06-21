package net.purplecraft.purplecraft;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Collections;

public class Purplecraft extends JavaPlugin {

    private Location spawnLocation;
    private final Map<UUID, GameMode> playerGamemodes = new HashMap<>();

    @Override
    public void onEnable() {
        // Load spawn location from config (optional)
        this.loadSpawnLocation();

        // Register commands and events
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommandExecutor(this));
        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawnCommandExecutor(this));
        Objects.requireNonNull(this.getCommand("speed")).setExecutor(new SetSpeedCommandExecutor(this));
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new TPACommandExecutor(this));
        Objects.requireNonNull(this.getCommand("tpaccept")).setExecutor(new TPAcceptCommandExecutor(this));
    }

    /*
        Spawn
     */

    private void loadSpawnLocation() {
        this.spawnLocation = this.getConfig().getLocation("spawn");
        if (spawnLocation == null) {
            getLogger().info("Spawn location not found in config, setting default spawn.");
            this.spawnLocation = this.getServer().getWorlds().get(0).getSpawnLocation();
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public static class SpawnCommandExecutor implements CommandExecutor {

        private final Purplecraft plugin;

        public SpawnCommandExecutor(Purplecraft plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
            return internalOnCommand(sender, cmd, label, args);
        }

        private boolean internalOnCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("purplecraft.spawn")) {
                sender.sendMessage(paris.PluginPrefix("You don't have permission to use /spawn!"));
                return false;
            }

            if (plugin.getSpawnLocation() == null) {
                sender.sendMessage(paris.PluginPrefix("Spawn location not set!"));
                return false;
            }

            Player player = (Player) sender;
            player.teleport(plugin.getSpawnLocation());
            sender.sendMessage(paris.PluginPrefix("Teleporting to spawn..."));

            return true;
        }
    }

    /*
        Set Spawn
     */

    public static class SetSpawnCommandExecutor implements CommandExecutor {

        private final Purplecraft plugin;

        public SetSpawnCommandExecutor(Purplecraft plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
            return internalOnCommand(sender, cmd, label, args);
        }

        private boolean internalOnCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("purplecraft.setspawn")) {
                sender.sendMessage(paris.PluginPrefix("You don't have permission to use /setspawn!"));
                return false;
            }

            if (sender instanceof Player) {
                Location location = ((Player) sender).getLocation();
                plugin.spawnLocation = location;
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

    /*
        Set Speed
     */
    public static class SetSpeedCommandExecutor implements CommandExecutor {

        private final Purplecraft plugin;

        public SetSpeedCommandExecutor(Purplecraft plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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

    /*
        TPA
     */

    private final Map<Player, Player> tpaRequests = new HashMap<>();

    public Map<Player, Player> getTPARequests() {
        return tpaRequests; // Return the modifiable map
    }

    public static class TPACommandExecutor implements CommandExecutor {

        private final Purplecraft plugin;

        public TPACommandExecutor(Purplecraft plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(paris.PluginPrefix("This command can only be used by players!"));
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(paris.PluginPrefix("/tpa <player>"));
                return true;
            }

            Player targetPlayer = plugin.getServer().getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(paris.PluginPrefix("Player not found!"));
                return true;
            }

            // Send teleport request to target player
            targetPlayer.sendMessage(paris.PluginPrefix(player.getName() + " has requested to teleport to you. /tpaccept to accept."));
            plugin.getTPARequests().put(targetPlayer, player); // Store request (replace with your implementation)

            sender.sendMessage(paris.PluginPrefix("Teleport request sent to " + targetPlayer.getName() + "."));
            return true;
        }
    }
    public static class TPAcceptCommandExecutor implements CommandExecutor {

        private final Purplecraft plugin;

        public TPAcceptCommandExecutor(Purplecraft plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(paris.PluginPrefix("This command can only be used by players!"));
                return true;
            }

            Player player = (Player) sender;

            // Check if there's a pending request for the player
            Player requester = plugin.getTPARequests().get(player);
            if (requester == null) {
                sender.sendMessage(paris.PluginPrefix("You don't have any pending teleport requests."));
                return true;
            }

            // Teleport player to requester's location
            player.teleport(requester.getLocation());
            plugin.getTPARequests().remove(player); // Remove request

            sender.sendMessage(paris.PluginPrefix("Teleported to " + requester.getName() + "."));
            requester.sendMessage(paris.PluginPrefix(player.getName() + " has accepted your teleport request."));

            return true;
        }
    }
}