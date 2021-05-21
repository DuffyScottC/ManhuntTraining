package me.braekpo1nt.commands.subcommands;

import me.braekpo1nt.commands.interfaces.SubCommand;
import me.braekpo1nt.commands.interfaces.SubTabCommand;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles starting the training program. Providing no arguments
 * starts the training program where it atuomattically chooses
 * Activities for you to train in. You can add arguments to start
 * specific training activities. 
 */
public class StartSubCommand implements SubTabCommand {
    
    private Main plugin;
    
    public StartSubCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                if (args[0].equals("random")) {
                    if (sender instanceof Player) {
                        plugin.getActivityManager().startRandomActivity((Player) sender);
                        return true;
                    } else {
                        sender.sendMessage("Only a player can run this command.");
                        return false;
                    }
                } else if (args[0].equals("all")) {
                    if (sender instanceof Player) {
                        plugin.getActivityManager().startTrainingCycle((Player) sender);
                        return true;
                    } else {
                        sender.sendMessage("Only a player can run this command.");
                        return false;
                    }
                } else if (!plugin.getActivityManager().hasActivity(args[0])) {
                    sender.sendMessage("Command not recognized. Please provide a valid argument to the \"start\" command.");
                    return false;
                } else {
                    if (sender instanceof Player) {
                        plugin.getActivityManager().startActivity(args[0], (Player) sender);
                        return true;
                    } else {
                        sender.sendMessage("Only a player can run this command.");
                        return false;
                    }
                }
            } else {
                sender.sendMessage("Please provide a valid argument.");
                return false;
            }
        } else {
            sender.sendMessage("Please provide a valid argument.");
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> results = new ArrayList<>(this.plugin.getActivityManager().getActivities().keySet());
            results.add("random");
            results.add("all");
            return results;
        } else {
            return null;
        }
    }
}
