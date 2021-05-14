package me.braekpo1nt.commands;

import me.braekpo1nt.commands.subcommands.CraftingSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * The command Manager for the "train" command.
 */
public class TrainCommandManager implements CommandManager {
    
    public TrainCommandManager() {
        subCommands.put("craft", new CraftingSubCommand());
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Please provide an argument.");
            return false;
        }
        
        if (!subCommands.containsKey(args[0])) {
            sender.sendMessage("Argument \"" + args[0] + "\" is not recognized.");
            return false;
        }
        
        return subCommands.get(args[0]).onCommand(sender, command, label, args);
    }

    /**
     * Returns a list of this CommandManager's SubCommands for tab completion.
     * @param sender
     * @param command
     * @param alias
     * @param args
     * @return The list of this CommandManager's SubCommand names
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subCommandNames = new ArrayList<>(subCommands.keySet());
        return subCommandNames;
    }
}
