package me.braekpo1nt.manhunttraining;

import me.braekpo1nt.commands.TrainCommandManager;
import me.braekpo1nt.listeners.CraftingListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("train").setExecutor(new TrainCommandManager());
        this.getServer().getPluginManager().registerEvents(new CraftingListener(), this);
    }

}
