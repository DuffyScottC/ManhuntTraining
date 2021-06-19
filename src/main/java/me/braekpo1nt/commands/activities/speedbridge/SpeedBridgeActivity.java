package me.braekpo1nt.commands.activities.speedbridge;

import me.braekpo1nt.commands.activities.abstracts.activities.ConfigurableActivity;
import me.braekpo1nt.commands.activities.speedbridge.configurers.SpeedBridgeBridgeAreaConfigurer;
import me.braekpo1nt.commands.activities.speedbridge.configurers.SpeedBridgeFinishAreaConfigurer;
import me.braekpo1nt.commands.activities.speedbridge.configurers.SpeedBridgeStartLocationConfigurer;
import me.braekpo1nt.commands.activities.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import me.braekpo1nt.visualizers.BoundingBoxVisualizer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class SpeedBridgeActivity extends ConfigurableActivity implements Activity {
    
    public static final String START_LOCATION = "speed-bridge.start-location";
    public static final String BRIDGE_AREA = "speed-bridge.bridge-area";
    public static final String FINISH_AREA = "speed-bridge.finish-area";
    
    private final Main plugin;
    private int heightLimit = 0;
    private Player player;
    /**
     * True if this activity is active, false if not.
     */
    private boolean isActive = false;
    /**
     * The location the player should start at
     */
    private BlockVector startLocation;
    /**
     * The area that will be set to air to clear the bridge blocks
     * the user places.
     */
    private BoundingBox bridgeArea;
    /**
     * The area the user has to step into to succeed in the
     * activity (e.i. the place to bridge to).
     */
    private BoundingBox finishArea;
    private final BoundingBoxVisualizer boundingBoxVisualizer;
    
    public SpeedBridgeActivity(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new SpeedBridgeListener(this), plugin);
        
        this.boundingBoxVisualizer = new BoundingBoxVisualizer(plugin);
        
        // Add new ActivityConfigurer objects here
        configurers.put("bridgearea", new SpeedBridgeBridgeAreaConfigurer(plugin));
        configurers.put("startlocation", new SpeedBridgeStartLocationConfigurer(plugin));
        configurers.put("finisharea", new SpeedBridgeFinishAreaConfigurer(plugin));
    }
    
    @Override
    public void start() {
        this.player = plugin.getActivityManager().getPlayer();
        Vector startLocationConf = plugin.getConfig().getVector(this.START_LOCATION);
        if (startLocationConf == null || !(startLocationConf instanceof BlockVector)) {
            player.sendMessage("Start location has not been set.");
            return;
        }
        this.startLocation = (BlockVector) startLocationConf;
        Object bridgeAreaConf = plugin.getConfig().get(this.BRIDGE_AREA);
        if (bridgeAreaConf == null || !(bridgeAreaConf instanceof BoundingBox)) {
            player.sendMessage("Bridge area has not been set.");
            return;
        }
        this.bridgeArea = (BoundingBox) bridgeAreaConf;
        Object finishAreaConf = plugin.getConfig().get(this.FINISH_AREA);
        if (finishAreaConf == null || !(finishAreaConf instanceof BoundingBox)) {
            player.sendMessage("Finish area has not been set.");
            return;
        }
        this.finishArea = (BoundingBox) finishAreaConf;

        boundingBoxVisualizer.setBoundingBox(bridgeArea);
        boundingBoxVisualizer.show(player);
        heightLimit = (int) bridgeArea.getMinY();
        player.setGameMode(GameMode.SURVIVAL);
        teleportPlayerToStart();
        resetBridgeArea();
        clearPlayersInventory();
        givePlayerBridgeBlocks();
        this.isActive = true;
        this.player.sendMessage("Start speed bridging!");
    }
    
    @Override
    public boolean isActive() {
        return this.isActive;
    }
    
    @Override
    public void stop() {
        resetBridgeArea();
        clearPlayersInventory();
        this.isActive = false;
        plugin.getActivityManager().onContinue();
        boundingBoxVisualizer.hide();
    }
    
    private void teleportPlayerToStart() {
        this.player.sendMessage("Teleporting you to speed bridge location: " + startLocation);
        this.player.teleport(startLocation.toLocation(this.player.getWorld(), player.getLocation().getYaw(), player.getLocation().getPitch()));
    }
    
    private void resetBridgeArea() {
        this.player.sendMessage("Resetting bridge area.");
        for (int x = bridgeArea.getMin().getBlockX(); x <= bridgeArea.getMax().getBlockX(); x++) {
            for (int y = bridgeArea.getMin().getBlockY(); y <= bridgeArea.getMax().getBlockY(); y++) {
                for (int z = bridgeArea.getMin().getBlockZ(); z <= bridgeArea.getMax().getBlockZ(); z++) {
                    this.player.getWorld().getBlockAt(new Location(this.player.getWorld(), x, y, z)).setType(Material.AIR);
                }
            }
        }
    }
    
    private void clearPlayersInventory() {
        this.player.getInventory().clear();
    }

    private void givePlayerBridgeBlocks() {
        this.player.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 64));
    }
    
    private void resetPlayersInventory() {
        clearPlayersInventory();
        givePlayerBridgeBlocks();
    }
    
    public void onFallBelowHeight() {
        teleportPlayerToStart();
        resetBridgeArea();
        resetPlayersInventory();
    }
    
    public boolean playerHasFallen() {
        return this.player.getLocation().getBlockY() < this.heightLimit;
    }

    /**
     * Tells if the player is in the finish area.
     * @return True if the player's location is contained in the finish area, 
     * false if not.
     */
    public boolean playerIsInFinishArea() {
        return finishArea.contains(this.player.getLocation().toVector());
    }
    
    public void onSuccess() {
        this.player.sendMessage("Success!");
        this.player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
        stop();
    }
}
