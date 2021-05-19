package me.braekpo1nt.commands;

import me.braekpo1nt.commands.activities.ActivityManager;
import me.braekpo1nt.commands.interfaces.CommandManager;
import me.braekpo1nt.commands.interfaces.SubCommand;
import me.braekpo1nt.commands.interfaces.SubTabCommand;
import me.braekpo1nt.commands.subcommands.OptSubCommand;
import me.braekpo1nt.commands.subcommands.StartSubCommand;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The command Manager for the "train" command.
 */
public class TrainCommandManager implements CommandManager {
    
    private final Main plugin;
    
    public TrainCommandManager(Main plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("train").setExecutor(this);
        subCommands.put("start", new StartSubCommand(plugin));
        subCommands.put("opt", new OptSubCommand(plugin));
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
        
        return subCommands.get(args[0]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
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
        if (args.length == 1) {
            List<String> subCommandNames = new ArrayList<>(subCommands.keySet());
            return subCommandNames;
        } else if (args.length > 1) {
            // return the arguments of the subcommand, if any exist
            if (subCommands.containsKey(args[0])) {
                SubCommand subCommand = subCommands.get(args[0]);
                if (subCommand instanceof SubTabCommand) {
                    SubTabCommand subTabCommand = (SubTabCommand) subCommand;
                    return subTabCommand.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
                }
            } else {
                return null;
            }
        }
        
        return null;
    }
}
