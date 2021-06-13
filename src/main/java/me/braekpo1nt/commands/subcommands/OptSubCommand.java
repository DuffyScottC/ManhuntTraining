package me.braekpo1nt.commands.subcommands;

import me.braekpo1nt.commands.activities.interfaces.Activity;
import me.braekpo1nt.commands.activities.interfaces.Configurable;
import me.braekpo1nt.commands.interfaces.SubTabCommand;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptSubCommand implements SubTabCommand {

    private Main plugin;
    
    public OptSubCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Must provide an activity to configure.");
            return false;
        } else if (args.length > 0) {
            if (!plugin.getActivityManager().hasActivity(args[0])) {
                sender.sendMessage("Activity \"" + args[0] + "\" does not exist. Please provide a valid activity to configure.");
                return false;
            } else {
                plugin.getActivityManager().configureActivity(sender, command, label, Arrays.copyOfRange(args, 1, args.length), args[0]);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(this.plugin.getActivityManager().getActivities().keySet());
        } else if (args.length > 1) {
            if (this.plugin.getActivityManager().hasActivity(args[0])) {

                Activity activity = this.plugin.getActivityManager().getActivities().get(args[0]);
                if (activity instanceof Configurable) {
                    Configurable configurableActivity = (Configurable) activity;
                    return configurableActivity.onConfigureTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length));
                }
                
            } else {
                return null;
            }
        }
        return null;
    }
}
