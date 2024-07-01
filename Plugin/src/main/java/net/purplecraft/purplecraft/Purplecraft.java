package net.purplecraft.purplecraft;
import net.purplecraft.purplecraft.chat.CustomJoinMessage;
import net.purplecraft.purplecraft.chat.CustomLeaveMessage;
import net.purplecraft.purplecraft.commands.*;
import net.purplecraft.purplecraft.commands.admin.*;
import net.purplecraft.purplecraft.economy.AccountManager;
import net.purplecraft.purplecraft.economy.CustomEconomy;
import net.purplecraft.purplecraft.economy.EconomyHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Purplecraft extends JavaPlugin {

    // General shizz
    public static String strmotd;
    public String getMOTD() { return strmotd; }
    private static AccountManager accountManager;
    public static CustomEconomy customEconomy;
    public static EconomyHandler economyHandler;
    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public static String removeCarriageReturn(String str) {
        return str.replaceAll("\\r", "");
    }
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {

        plugin = this;

        // Load spawn location from config (optional)
        Spawn.loadSpawnLocation();

        // Register commands and events
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new Spawn());
        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawn());
        getServer().getPluginManager().registerEvents(new SetSpawn(), this);
        Objects.requireNonNull(this.getCommand("speed")).setExecutor(new Speed());
        Objects.requireNonNull(this.getCommand("tpa")).setExecutor(new Teleport());
        Objects.requireNonNull(this.getCommand("tpaccept")).setExecutor(new Teleport());
        Objects.requireNonNull(this.getCommand("motd")).setExecutor(new MOTD());
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new Fly());
        Objects.requireNonNull(this.getCommand("god")).setExecutor(new God());
        getServer().getPluginManager().registerEvents(new MOTD.JoinMOTDListener(), this);

        // SelectPos
        Bukkit.getPluginManager().registerEvents(new SelectPos(), this);
        Objects.requireNonNull(this.getCommand("selectpos")).setExecutor(new SelectPos());

        // Economy stuff
        accountManager = new AccountManager();
        customEconomy = new CustomEconomy(this);
        economyHandler = new EconomyHandler();

        try {
            accountManager.loadBalances();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!EconomyHandler.setupEconomy()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(economyHandler, this);
        Objects.requireNonNull(getCommand("bal")).setExecutor(new Balance());
        Objects.requireNonNull(getCommand("balance")).setExecutor(new Balance());
        Objects.requireNonNull(getCommand("eco")).setExecutor(new Eco());
        Objects.requireNonNull(getCommand("pay")).setExecutor(new Pay());
        Objects.requireNonNull(getCommand("baltop")).setExecutor(new BalanceTop());
        Objects.requireNonNull(getCommand("balancetop")).setExecutor(new BalanceTop());

        // Chat stuff!
        getServer().getPluginManager().registerEvents(new CustomJoinMessage(), this);
        getServer().getPluginManager().registerEvents(new CustomLeaveMessage(), this);

        // Read MOTD from file
        File motdFile = new File(getDataFolder(), "motd.txt");
        if (motdFile.exists()) {
            try {
                strmotd = new String(Files.readAllBytes(Paths.get(motdFile.getPath())));
                strmotd = removeCarriageReturn(strmotd);
            } catch (IOException e) {
                getLogger().warning("Error reading MOTD file: " + e.getMessage());
            }
        } else {
            getLogger().info("MOTD file not found. Creating a new one...");
            try {
                boolean fileExistsAlready = motdFile.createNewFile();
                if (!fileExistsAlready) {
                    strmotd = "Welcome to the server!";
                    Files.write(Paths.get(motdFile.getPath()), strmotd.getBytes());
                }
            } catch (IOException e) {
                getLogger().warning("Error creating MOTD file: " + e.getMessage());
                strmotd = "MOTD could not be loaded.";
            }
        }

        getLogger().info("Purplecraft enabled.");

    }

}