package me.braekpo1nt.commands.activities.speedbridge.configurers;

import me.braekpo1nt.commands.activities.abstracts.configurers.AreaConfigurer;
import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.manhunttraining.Main;

public class SpeedBridgeBridgeAreaConfigurer extends AreaConfigurer {
    
    private Main plugin;
    
    public SpeedBridgeBridgeAreaConfigurer(Main plugin) {
        super(plugin);
    }

    @Override
    protected String getConfigString() {
        return SpeedBridgeActivity.BRIDGE_AREA;
    }


}
