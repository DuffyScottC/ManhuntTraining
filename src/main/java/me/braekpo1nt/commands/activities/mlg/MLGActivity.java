package me.braekpo1nt.commands.activities.mlg;

import me.braekpo1nt.commands.activities.abstracts.ConfigurableActivity;
import me.braekpo1nt.commands.activities.mlg.configurers.MLGStartLocationConfigurer;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class MLGActivity extends ConfigurableActivity implements Activity {
    
    public static final String START_LOCATION = "mlg.start-location";
    
    private final Main plugin;
    private Player player;
    private GameMode oldGamemode;
    private boolean active = false;
    
    private BlockVector startLocation;
    
    public MLGActivity(Main plugin) {
        this.plugin = plugin;
        configurers.put("startlocation", new MLGStartLocationConfigurer(plugin));
    }
    
    @Override
    public void start(Player player) {
        Vector startLocationConf = plugin.getConfig().getVector(this.START_LOCATION);
        if (startLocationConf == null || !(startLocationConf instanceof BlockVector)) {
            player.sendMessage("Start location has not been set.");
            return;
        }
        this.startLocation = (BlockVector) startLocationConf;
        this.player = player;
        active = true;
        oldGamemode = player.getGameMode();
        player.setGameMode(GameMode.SURVIVAL);
        teleportPlayerToStart();
        assignMLG();
        player.sendMessage("MLG!");
    }

    private void teleportPlayerToStart() {
        this.player.teleport(startLocation.toLocation(this.player.getWorld(), 0, 90));
    }

    private void assignMLG() {
        this.player.getInventory().clear();
        this.player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
    }

    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public void stop() {
        player.setGameMode(oldGamemode);
        active = false;
    }
}
