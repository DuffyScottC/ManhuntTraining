package me.braekpo1nt.commands.interfaces;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This interface is implemented by Activities. Each Activity is
 * a thing to train on, to practice.
 * 
 * You have to make a new Activity class, implement this interface,
 * add it to the ActivityManager
 */
public interface Activity {
    
    void start();
    boolean isActive();
    void stop();
    
}
