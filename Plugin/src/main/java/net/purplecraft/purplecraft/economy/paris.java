package net.purplecraft.purplecraft.economy;

public class paris {

    public static final String HEADER_CHAR = "§5§l|";
    public static final String ECONOMY_PREFIX = "§a§l$§r";
    public static final String ECONOMY_SUFFIX = " §a§lbux§r";
    public static final String TEXT_COLOR = "§r§d";

    public static double STARTING_BUX = 100;

    public static String PluginPrefix(String message) {
        return HEADER_CHAR + TEXT_COLOR + " " + message;
    }

}
