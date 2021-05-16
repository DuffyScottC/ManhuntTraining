package me.braekpo1nt.manhunttraining;

import me.braekpo1nt.commands.TrainCommandManager;
import me.braekpo1nt.commands.activities.ActivityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    
    private ActivityManager activityManager;
    
    @Override
    public void onEnable() {
        activityManager = new ActivityManager(this);
        new TrainCommandManager(this);
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }
}
