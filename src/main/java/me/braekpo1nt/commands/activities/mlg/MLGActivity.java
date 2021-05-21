package me.braekpo1nt.commands.activities.mlg;

import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.entity.Player;

public class MLGActivity implements Activity {
    
    private final Main plugin;
    
    public MLGActivity(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void start(Player player) {
        player.sendMessage("starting mlg");
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void stop() {
        
    }
}
