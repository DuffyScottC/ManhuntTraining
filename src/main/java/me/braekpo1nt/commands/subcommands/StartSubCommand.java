package me.braekpo1nt.commands.subcommands;

import me.braekpo1nt.commands.interfaces.SubCommand;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This starts the overall training game. Essentially calls the ActivityManager
 * to choose a random first activity. 
 */
public class StartSubCommand implements SubCommand {
    
    private Main plugin;
    
    public StartSubCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getActivityManager().startRandomActivity((Player) sender);
        return true;
    }
    
}
