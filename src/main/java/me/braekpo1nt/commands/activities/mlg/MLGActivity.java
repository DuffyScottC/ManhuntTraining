package me.braekpo1nt.commands.activities.mlg;

import me.braekpo1nt.commands.activities.ActivityManager;
import me.braekpo1nt.commands.activities.abstracts.ConfigurableActivity;
import me.braekpo1nt.commands.activities.mlg.configurers.MLGChunkConfigurer;
import me.braekpo1nt.commands.activities.mlg.configurers.MLGStartLocationConfigurer;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.Random;

public class MLGActivity extends ConfigurableActivity implements Activity {
    
    @Deprecated
    public static final String START_LOCATION = "mlg.start-location";
    public static final String CHUNK = "mlg.chunk";

    private final Main plugin;
    private Player player;
    private GameMode oldGamemode;
    private boolean active = false;
    
    private Chunk chunk;
    private final int LOWEST_HEIGHT = 10;
    private final int HIGHEST_HEIGHT = 100;
    
    public MLGActivity(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new MLGListener(this, plugin), plugin);
        
        configurers.put("startlocation", new MLGStartLocationConfigurer(plugin));
        configurers.put("chunk", new MLGChunkConfigurer(plugin));
    }
    
    @Override
    public void start() {
        this.player = plugin.getActivityManager().getPlayer();
        Vector chunkVectorConf = plugin.getConfig().getVector(CHUNK);
        if (chunkVectorConf == null || !(chunkVectorConf instanceof BlockVector)) {
            player.sendMessage("Chunk has not been set.");
            return;
        }
        BlockVector chunkVector = (BlockVector) chunkVectorConf;
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
        Location startLocation = getRandomStartLocation();
        this.player.teleport(startLocation);
    }

    /**
     * Returns a random start location within the chunk. It will always be at least as high as 
     * LOWEST_HEIGHT blocks above the block directly below the x,z position
     * @return a random start location in the chunk
     */
    private Location getRandomStartLocation() {
        // pitch=0,yaw=90
        Random random = new Random();
        // Pick a position
        int x = random.nextInt(14);
        int z = random.nextInt(14);
        // Find the ground
        boolean isAir = true;
        int groundY = chunk.getWorld().getMaxHeight() - 1;
        while (isAir) {
            isAir = chunk.getBlock(x, groundY, z).getType().isAir();
            groundY--;
            //edge case for if there are 0 non-air blocks
            if (groundY < chunk.getWorld().getMinHeight()) {
                isAir = false;
                groundY = chunk.getWorld().getMaxHeight() - 1;
            }
        }
        // pick a y
        int y = groundY + (LOWEST_HEIGHT + random.nextInt(HIGHEST_HEIGHT - LOWEST_HEIGHT));
        // create the location
        Block block = chunk.getBlock(x, y, z);
        return block.getLocation().add(new Vector(random.nextFloat(), random.nextFloat(), random.nextFloat()));
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
        this.player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
        stop();
    }
    
    @Override
    public void stop() {
        player.setGameMode(oldGamemode);
        active = false;
        plugin.getActivityManager().onContinue();
    }
}
