package me.braekpo1nt.commands.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface Configurable {
    /**
     * Called when this should be configured using the passed
     * in arguments.
     * @param args The command line arguments to use for
     *             Configurable.
     */
    boolean onConfigure(CommandSender sender, Command command, String label, String[] args);
    
    /**
     * Called when the user is typing in arguments to this Configurable.
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    List<String> onConfigureTabComplete(CommandSender sender, Command command, String label, String[] args);
}
