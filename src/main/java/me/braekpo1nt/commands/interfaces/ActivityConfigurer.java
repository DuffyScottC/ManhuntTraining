package me.braekpo1nt.commands.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ActivityConfigurer {
    boolean onConfigure(CommandSender sender, Command command, String label, String[] args);
    List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args);
}
