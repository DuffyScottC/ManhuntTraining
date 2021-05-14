package me.braekpo1nt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * The command Manager for the "train" command.
 */
public class TrainCommandManager implements CommandManager {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> subCommandNames = new ArrayList<>(subCommands.keySet());
        return subCommandNames;
    }
}
