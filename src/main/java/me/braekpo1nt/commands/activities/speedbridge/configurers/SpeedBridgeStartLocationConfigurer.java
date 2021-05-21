package me.braekpo1nt.commands.activities.speedbridge.configurers;

import me.braekpo1nt.commands.activities.abstracts.configurers.LocationConfigurer;
import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.manhunttraining.Main;

public class SpeedBridgeStartLocationConfigurer extends LocationConfigurer {
    
    public SpeedBridgeStartLocationConfigurer(Main plugin) {
        super(plugin);
    }
    
    @Override
    protected String getConfigString() {
        return SpeedBridgeActivity.START_LOCATION;
    }
    
}
