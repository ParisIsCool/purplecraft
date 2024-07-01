package net.purplecraft.purplecraft.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.purplecraft.purplecraft.economy.paris.HEADER_CHAR;

public class CustomJoinMessage implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String message = String.format(HEADER_CHAR + ChatColor.GREEN + ChatColor.BOLD + " + " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "%s", player.getName()); // Customize the message format here
        // You can use PlaceholderAPI for more dynamic messages (https://www.spigotmc.org/resources/placeholderapi.7745/)
        event.setJoinMessage(message);
    }
}
