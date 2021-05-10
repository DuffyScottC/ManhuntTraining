package me.braekpo1nt.manhunttraining;

import me.braekpo1nt.commands.CraftingCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        new CraftingCommand(this);
    }

}
