package net.purplecraft.purplecraft.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.purplecraft.purplecraft.economy.paris.HEADER_CHAR;

public class CustomLeaveMessage implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String message = String.format(HEADER_CHAR + ChatColor.RED + ChatColor.BOLD + " - " + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "%s", player.getName()); // Customize the message format here
        event.setQuitMessage(message);
    }
}
