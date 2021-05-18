package me.braekpo1nt.commands.interfaces;

import org.bukkit.entity.Player;

/**
 * This interface is implemented by Activities. Each Activity is
 * a thing to train on, to practice.
 * 
 * You have to make a new Activity class, implement this interface,
 * add it to the ActivityManager
 */
public interface Activity {
    
    void start(Player player);
    void stop();
    
}
