package me.braekpo1nt.commands.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This interface is implemented by Activities. Each Activity is
 * a thing to train on, to practice.
 * 
 * You have to make a new Activity class, implement this interface,
 * add it to the ActivityManager
 */
public interface Activity {

    /**
     * A map of {@link ActivityConfigurer}s for this {@link Activity}.
     * Maps configurer option names to their classes.
     * Add {@link ActivityConfigurer}s to this list to enable the
     * configuration of this activity.
     */
    Map<String, ActivityConfigurer> configurers = new HashMap<>();
    
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
    
    List<String> onConfigureTabComplete(CommandSender sender, Command command, String label, String[] args);
}
