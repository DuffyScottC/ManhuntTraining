package me.braekpo1nt.manhunttraining;

import me.braekpo1nt.commands.TrainCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("train").setExecutor(new TrainCommandManager());
    }

}
