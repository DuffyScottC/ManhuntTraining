package me.braekpo1nt.commands;

import me.braekpo1nt.commands.subcommands.SubCommand;
import org.bukkit.command.TabExecutor;

import java.util.Map;

/**
 * This interface is implemented by command managers looking to manage
 * a set of objects that implement the {
 * @link me.braekpo1nt.commands.subcommands.SubCommand} interface.
 */
public interface CommandManager extends TabExecutor {

    /**
     * Maps SubComands by their name. 
     */
    Map<String, SubCommand> subCommands = null;
    
}
