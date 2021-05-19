package me.braekpo1nt.commands.subcommands;

import me.braekpo1nt.commands.interfaces.SubTabCommand;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                plugin.getActivityManager().configureActivity(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
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
                return this.plugin.getActivityManager().getActivities().get(args[0]).onConfigureTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
            } else {
                return null;
            }
        }
        return null;
    }
}
