package me.braekpo1nt.commands.activities;

import me.braekpo1nt.commands.activities.crafting.CraftingActivity;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the Activities. 
 * - Chooses new activities based on randomness
 * - Starts specific activities
 * - Maintains the list of activities
 */
public class ActivityManager {
    
    List<Activity> activities = new ArrayList<>();
    
    public ActivityManager(Main plugin) {
        activities.add(new CraftingActivity(plugin));
    }
    
    public void startRandomActivity(Player player) {
        activities.get(0).start(player);
    }
    
}
