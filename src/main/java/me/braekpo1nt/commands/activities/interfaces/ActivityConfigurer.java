package me.braekpo1nt.commands.activities.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ActivityConfigurer {
    
    /**
     * Contains configuration logic. Uses the passed in args to configure
     * the associated Activity.
     * @param sender the sender
     * @param command the command
     * @param label the label
     * @param args the configuration arguments
     * @return true if successful, false if not
     */
    boolean onConfigure(CommandSender sender, Command command, String label, String[] args);
    
    /**
     * Returns the tab completion options for the Configurer
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
    
}
