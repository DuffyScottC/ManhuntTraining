package me.braekpo1nt.commands.activities.mlg;

import me.braekpo1nt.commands.activities.ActivityManager;
import me.braekpo1nt.commands.activities.abstracts.ConfigurableActivity;
import me.braekpo1nt.commands.activities.mlg.configurers.MLGChunkConfigurer;
import me.braekpo1nt.commands.activities.mlg.configurers.MLGStartLocationConfigurer;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class MLGActivity extends ConfigurableActivity implements Activity {
    
    public static final String START_LOCATION = "mlg.start-location";
    public static final String CHUNK = "mlg.chunk";

    private final Main plugin;
    private Player player;
    private GameMode oldGamemode;
    private boolean active = false;
    
    private BlockVector startLocation;
    private Chunk chunk;
    
    public MLGActivity(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new MLGListener(this, plugin), plugin);
        
        configurers.put("startlocation", new MLGStartLocationConfigurer(plugin));
        configurers.put("chunk", new MLGChunkConfigurer(plugin));
    }
    
    @Override
    public void start() {
        Vector startLocationConf = plugin.getConfig().getVector(this.START_LOCATION);
        if (startLocationConf == null || !(startLocationConf instanceof BlockVector)) {
            player.sendMessage("Start location has not been set.");
            return;
        }
        this.startLocation = (BlockVector) startLocationConf;
        Vector chunkVectorConf = plugin.getConfig().getVector(this.CHUNK);
        if (chunkVectorConf == null || !(chunkVectorConf instanceof BlockVector)) {
            player.sendMessage("Chunk has not been set.");
            return;
        }
        BlockVector chunkVector = (BlockVector) chunkVectorConf;
        this.player = plugin.getActivityManager().getPlayer();
        this.chunk = player.getWorld().getChunkAt(chunkVector.getBlockX(), chunkVector.getBlockZ());
        active = true;
        oldGamemode = player.getGameMode();
        player.setGameMode(GameMode.SURVIVAL);
        teleportPlayerToStart();
        assignMLG();
        player.sendMessage("MLG!");
    }
    
    public void retry() {
        teleportPlayerToStart();
        assignMLG();
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
    
    public boolean playerIsOnGround() {
        return player.getLocation().clone().subtract(0,0.1,0).getBlock().getType().isSolid();
    }
    
    public void onSuccess() {
        player.sendMessage("Success!");
        stop();
    }
    
    @Override
    public void stop() {
        player.setGameMode(oldGamemode);
        active = false;
        plugin.getActivityManager().onContinue();
    }
}
