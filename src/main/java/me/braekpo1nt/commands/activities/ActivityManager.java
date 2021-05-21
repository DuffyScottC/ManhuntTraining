package me.braekpo1nt.commands.activities;

import me.braekpo1nt.commands.activities.crafting.CraftingActivity;
import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.commands.interfaces.Configurable;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages the Activities. 
 * - Chooses new activities based on randomness
 * - Starts specific activities
 * - Maintains the list of activities
 */
public class ActivityManager {
    
    Map<String, Activity> activities = new HashMap<>();
    
    public ActivityManager(Main plugin) {
        /*
        Add new activities here, pairing them with their name.
         */
        activities.put("craft", new CraftingActivity(plugin));
        activities.put("speedbridge", new SpeedBridgeActivity(plugin));
    }

    /**
     * Chooses a random activity from the list of activities to start.
     * @param player The player to start the activities for. 
     */
    public void startRandomActivity(Player player) {
        player.sendMessage("Randomly choosing an activity...");
        Random random = new Random();
        int randomIndex = random.nextInt(activities.size());
        Bukkit.getLogger().info("random index for activity: " + Integer.toString(randomIndex));
        activities.get(activities.keySet().toArray()[randomIndex]).start(player);
    }
    
    public Map<String, Activity> getActivities() {
        return this.activities;
    }
    
    /**
     * Checks if an activity with the given name exists in the list of activities.
     * @param activityName
     * @return True if an activity with the given name exists, false if not. 
     */
    public boolean hasActivity(String activityName) {
        return this.activities.containsKey(activityName);
    }
    
    /**
     * Starts the given Activity.
     * @param activityName The name of the activity to start
     * @param player The player to pass to the activity
     */
    public void startActivity(String activityName, Player player) {
        if (!activities.containsKey(activityName)) {
            player.sendMessage("Activity with name \"" + activityName + "\" could not be found.");
            return;
        }
        activities.get(activityName).start(player);
    }
    
    /**
     * Configures the given Activity.
     * @param activityName The name of the Activity to configure
     * @param args The args to configure the activity 
     */
    public void configureActivity(CommandSender sender, Command command, String label, String[] args, String activityName) {
        if (activities.containsKey(activityName)) {
            Activity activity = activities.get(activityName);
            if (activity instanceof Configurable) {
                Configurable configurableActivity = (Configurable) activity;
                configurableActivity.onConfigure(sender, command, label, args);
            }
        }
    }
}
