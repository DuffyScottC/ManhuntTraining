package me.braekpo1nt.commands.subcommands;

import me.braekpo1nt.commands.interfaces.SubCommand;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StopSubCommand implements SubCommand {
    
    private Main plugin;
    
    public StopSubCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getActivityManager().onQuit();
        return true;
    }
}
