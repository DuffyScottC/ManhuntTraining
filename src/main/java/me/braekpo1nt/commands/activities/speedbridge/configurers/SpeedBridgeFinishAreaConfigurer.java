package me.braekpo1nt.commands.activities.speedbridge.configurers;

import me.braekpo1nt.commands.activities.abstracts.AreaConfigurer;
import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.manhunttraining.Main;

public class SpeedBridgeFinishAreaConfigurer extends AreaConfigurer {
    
    public SpeedBridgeFinishAreaConfigurer(Main plugin) {
        super(plugin);
    }
    
    @Override
    protected String getConfigString() {
        return SpeedBridgeActivity.FINISH_AREA;
    }

}
