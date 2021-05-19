package me.braekpo1nt.commands.activities.crafting.configurers;

import me.braekpo1nt.commands.activities.abstracts.AreaConfigurer;
import me.braekpo1nt.commands.activities.crafting.CraftingActivity;
import me.braekpo1nt.manhunttraining.Main;

public class CraftingStartLocationConfigurer extends LocationConfigurer {
    
    public CraftingStartLocationConfigurer(Main plugin) {
        super(plugin);
    }

    @Override
    protected String getConfigString() {
        return CraftingActivity.START_LOCATION;
    }

}
