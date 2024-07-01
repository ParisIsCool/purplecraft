package net.purplecraft.purplecraft.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SelectPos implements CommandExecutor, Listener {
    private final static float SOUTH_YAW_MIN = -18.0f;
    private final static float SOUTH_YAW_MAX = 18.0f;
    private final static float SOUTH_PITCH_MIN = -14.6f;
    private final static float SOUTH_PITCH_MAX = 9.6f;
    private final static double WORLD_WIDTH = 73728.0;
    private final static double WORLD_HEIGHT = 36864.0;

    private final List<Player> frozenPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            teleportAndPreparePlayer(player);
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
        return true;
    }

    private void teleportAndPreparePlayer(Player player) {
        player.teleport(new Location(player.getWorld(), 0.5, 311, 0.5));
        player.setGameMode(GameMode.SURVIVAL);

        // Freeze player and give godmode
        frozenPlayers.add(player);
        player.setInvulnerable(true);
        player.setWalkSpeed(((float) 0));
        player.setFlySpeed(((float) 0));

        // Notify the player
        player.sendMessage("You have been teleported. Please click to select your position.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player) && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();

            // Check if the player's yaw and pitch are within the specified ranges
            if (isAngleWithinRange(yaw, SOUTH_YAW_MIN, SOUTH_YAW_MAX) && isAngleWithinRange(pitch, SOUTH_PITCH_MIN, SOUTH_PITCH_MAX)) {
                Vector clickDirection = calculateDirectionVector(yaw, pitch);
                Location newLocation = clickDirection.multiply(new Vector(WORLD_WIDTH, 0, WORLD_HEIGHT)).toLocation(player.getWorld());

                // Teleport the player
                player.teleport(newLocation);

                // Unfreeze player and remove godmode
                frozenPlayers.remove(player);
                player.setInvulnerable(false);
                player.setWalkSpeed(((float) 0.4));
                player.setFlySpeed(((float) 0.2));

                player.sendMessage("You have been teleported to your selected position.");
            } else {
                player.sendMessage("Your click was not within the specified angle ranges. Try again.");
            }
        }
    }

    private boolean isAngleWithinRange(float angle, float min, float max) {
        return angle >= min && angle <= max;
    }

    private Vector calculateDirectionVector(float yaw, float pitch) {
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);

        double x = -Math.cos(pitchRad) * Math.sin(yawRad);
        double y = -Math.sin(pitchRad);
        double z = Math.cos(pitchRad) * Math.cos(yawRad);

        return new Vector(x, y, z);
    }
}