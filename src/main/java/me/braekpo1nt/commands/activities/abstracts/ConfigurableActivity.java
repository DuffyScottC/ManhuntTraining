package me.braekpo1nt.commands.activities.abstracts;

import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.commands.interfaces.ActivityConfigurer;
import me.braekpo1nt.commands.interfaces.Configurable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Convenience abstract class for easily adding and using new 
 * {@link ActivityConfigurer}s to {@link Activity}s
 * Does the heavy lifting of calling the methods of the 
 * {@link ActivityConfigurer}s' methods given the first argument 
 * matches the key in the configurers list.
 */
public abstract class ConfigurableActivity implements Configurable {

    /**
     * A map of {@link ActivityConfigurer}s for this {@link Activity}.
     * Maps configurer option names to their classes.
     * Add {@link ActivityConfigurer}s to this list to enable the
     * configuration of this activity.
     */
    protected final Map<String, ActivityConfigurer> configurers = new HashMap<>();

    /**
     * Attempts to call the onConfigure method of the {@link ActivityConfigurer} with the key matching the first element of the args in the configurers map.
     * @param sender the sender
     * @param command the command
     * @param label the label
     * @param args The command line arguments to use for configuring, where the first element is the key for a corresponding {@link ActivityConfigurer} in the configurers list.
     * @return
     */
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (configurers.containsKey(args[0])) {
                return configurers.get(args[0]).onConfigure(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            } else {
                sender.sendMessage("Provide a valid option. \"" + args[0] + "\" is not a recognized option.");
                return false;
            }
        } else {
            sender.sendMessage("Please provide a valid option argument.");
            return false;
        }
    }

    /**
     * Returns the keys of the configurers list if the args.length is 1, or the
     * return value of the {@link ActivityConfigurer} which matches
     * the first argument's onTabComplete().
     * @param sender the sender
     * @param command the command
     * @param label the label
     * @param args The command line arguements
     * @return
     */
    @Override
    public List<String> onConfigureTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(configurers.keySet());
        } else if (args.length > 1) {
            if (configurers.containsKey(args[0])) {
                return configurers.get(args[0]).onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
