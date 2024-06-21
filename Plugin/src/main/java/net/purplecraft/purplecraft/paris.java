package net.purplecraft.purplecraft;

public class paris {

    private static final String HEADER_CHAR = "§5§l|";
    private static final String TEXT_COLOR = "§d";

    public static String PluginPrefix(String message) {
        return HEADER_CHAR + TEXT_COLOR + " " + message;
    }

}
