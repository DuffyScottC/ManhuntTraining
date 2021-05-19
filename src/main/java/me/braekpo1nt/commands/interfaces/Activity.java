package me.braekpo1nt.commands.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This interface is implemented by Activities. Each Activity is
 * a thing to train on, to practice.
 * 
 * You have to make a new Activity class, implement this interface,
 * add it to the ActivityManager
 */
public interface Activity {
    
    void start(Player player);
    boolean isActive();
    void stop();

    /**
     * Allows the user to configure the Activity with passed
     * in arguments.
     * @param args The command line arguments to use for
     *             configuration.
     */
    boolean configure(CommandSender sender, Command command, String label, String[] args);
    
    List<String> onConfigureTabComplete(CommandSender sender, String[] args);
}
