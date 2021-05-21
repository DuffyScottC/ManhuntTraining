package me.braekpo1nt.commands.activities.mlg.configurers;

import me.braekpo1nt.commands.activities.abstracts.configurers.LocationConfigurer;
import me.braekpo1nt.commands.activities.mlg.MLGActivity;
import me.braekpo1nt.manhunttraining.Main;

public class MLGStartLocationConfigurer extends LocationConfigurer {
    
    public MLGStartLocationConfigurer(Main plugin) {
        super(plugin);
    } 
    
    @Override
    protected String getConfigString() {
        return MLGActivity.START_LOCATION;
    }
}
