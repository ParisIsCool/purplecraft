package net.purplecraft.purplecraft.commands;

import net.purplecraft.purplecraft.Purplecraft;
import net.purplecraft.purplecraft.economy.paris;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Teleport implements CommandExecutor {
    private final Map<Player, Player> tpaRequests = new HashMap<>();

    public Map<Player, Player> getTPARequests() {
        return tpaRequests; // Return the modifiable map
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tpa")) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(paris.PluginPrefix("This command can only be used by players!"));
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(paris.PluginPrefix("/tpa <player>"));
                return true;
            }

            Player targetPlayer = Purplecraft.plugin.getServer().getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(paris.PluginPrefix("Player not found!"));
                return true;
            }

            // Send teleport request to target player
            targetPlayer.sendMessage(paris.PluginPrefix(player.getName() + " has requested to teleport to you. /tpaccept to accept."));
            getTPARequests().put(targetPlayer, player); // Store request (replace with your implementation)

            sender.sendMessage(paris.PluginPrefix("Teleport request sent to " + targetPlayer.getName() + "."));
            return true;
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(paris.PluginPrefix("This command can only be used by players!"));
                return true;
            }

            // Check if there's a pending request for the player
            Player requester = getTPARequests().get(player);
            if (requester == null) {
                sender.sendMessage(paris.PluginPrefix("You don't have any pending teleport requests."));
                return true;
            }

            // Teleport player to requester's location
            player.teleport(requester.getLocation());
            getTPARequests().remove(player); // Remove request

            sender.sendMessage(paris.PluginPrefix("Teleported to " + requester.getName() + "."));
            requester.sendMessage(paris.PluginPrefix(player.getName() + " has accepted your teleport request."));

            return true;
        }
    }
}
