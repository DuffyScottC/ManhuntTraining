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
        if (args.length == 0) {
            plugin.getActivityManager().startRandomActivity((Player) sender);
            return true;
        } else if (args.length == 1) {
            if (!plugin.getActivityManager().hasActivity(args[0])) {
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
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(this.plugin.getActivityManager().getActivities().keySet());
        } else {
            return null;
        }
    }
}
