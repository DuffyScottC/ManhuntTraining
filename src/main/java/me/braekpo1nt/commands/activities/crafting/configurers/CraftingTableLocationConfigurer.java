package me.braekpo1nt.commands.activities.crafting.configurers;

import me.braekpo1nt.commands.activities.abstracts.configurers.LocationConfigurer;
import me.braekpo1nt.commands.activities.crafting.CraftingActivity;
import me.braekpo1nt.manhunttraining.Main;

public class CraftingTableLocationConfigurer extends LocationConfigurer {
    
    public CraftingTableLocationConfigurer(Main plugin) {
        super(plugin);
    }

    @Override
    protected String getConfigString() {
        return CraftingActivity.TABLE_LOCATION;
    }

}
