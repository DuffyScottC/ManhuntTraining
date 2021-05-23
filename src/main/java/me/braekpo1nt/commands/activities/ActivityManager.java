package me.braekpo1nt.commands.activities;

import me.braekpo1nt.commands.activities.crafting.CraftingActivity;
import me.braekpo1nt.commands.activities.mlg.MLGActivity;
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
    
    private final Main plugin;
    private final Map<String, Activity> activities = new HashMap<>();
    private Activity activeActivity = null;
    private boolean inTrainingCycle = false;
    /**
     * The player who is performing the training.
     */
    private Player player;
    
    private int continueTaskID;
    
    public ActivityManager(Main plugin) {
        this.plugin = plugin;
        /*
        Add new activities here, pairing them with their name.
         */
        activities.put("craft", new CraftingActivity(plugin));
        activities.put("speedbridge", new SpeedBridgeActivity(plugin));
        activities.put("mlg", new MLGActivity(plugin));
    }
    
    /**
     * Gets the player who is performing the training.
     * @return The player who is performing the training.
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Chooses a random activity from the list of activities to start.
     * @param player The player to start the activities for. 
     */
    public void startRandomActivity(Player player) {
        this.player = player;
        if (activeActivity != null && activeActivity.isActive()) {
            activeActivity.stop();
        }
        player.sendMessage("Randomly choosing an activity...");
        Random random = new Random();
        int randomIndex = random.nextInt(activities.size());
        Bukkit.getLogger().info("random index for activity: " + Integer.toString(randomIndex));
        activeActivity = activities.get(activities.keySet().toArray()[randomIndex]);
        activeActivity.start();
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
        this.player = player;
        if (!activities.containsKey(activityName)) {
            player.sendMessage("Activity with name \"" + activityName + "\" could not be found.");
            return;
        }
        if (activeActivity != null && activeActivity.isActive()) {
            activeActivity.stop();
        }
        activeActivity = activities.get(activityName);
        activeActivity.start();
    }

    /**
     * Kicks off the training cycle, wherein the player is
     * continuously put through successive random training
     * activities.
     * @param player The player to train.
     */
    public void startTrainingCycle(Player player) {
        inTrainingCycle = true;
        startRandomActivity(player);
    }
    
    /**
     * Called when the ActivityManager should continue. 
     * - If we're in the training cycle, this moves on to the next task
     * - If we're not in the training cycle, this does nothing.
     */
    public void onContinue() {
        if (activeActivity != null && activeActivity.isActive()) {
            activeActivity.stop();
        }
        if (inTrainingCycle) {
            Player schedulePlayer = this.player;
            continueTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                
                int countStart = 3;
                int count = countStart;
                
                @Override
                public void run() {
                    if (count <= 0) {
                        startRandomActivity(schedulePlayer);
                        Bukkit.getScheduler().cancelTask(continueTaskID);
                    } else if (count == countStart) {
                        schedulePlayer.sendMessage("Next task in:");
                        schedulePlayer.sendMessage(Integer.toString(count));
                    } else {
                        schedulePlayer.sendMessage(Integer.toString(count));
                    }
                    count -= 1;
                }
            }, 0, 20);
        }
    }
    
    public boolean isInTrainingCycle() {
        return this.inTrainingCycle;
    }

    /**
     * Called when the player wants to quit training.
     * This stops the active Activity and stops the training cycle.
     */
    public void onQuit() {
        if (activeActivity != null && activeActivity.isActive()) {
            activeActivity.stop();
        }
        player = null;
        inTrainingCycle = false;
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
