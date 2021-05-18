package me.braekpo1nt.commands.activities.speedbridge;

import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

public class SpeedBridgeActivity implements Activity {
    
    private int heightLimit = 92;
    private int finishLine = -157;
    private Player player;
    /**
     * True if this activity is active, false if not.
     */
    private boolean isActive = false;
    
    public SpeedBridgeActivity(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SpeedBridgeListener(this), plugin);
    }
    
    @Override
    public void start(Player player) {
        this.player = player;
        teleportPlayerToStart();
        resetBridgeArea();
        this.player.getInventory().clear();
        this.player.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 64));
        this.isActive = true;
        this.player.sendMessage("Start speed bridging!");
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    private void teleportPlayerToStart() {
        Location speedBridgeLocation = new Location(this.player.getWorld(), 162, 93, -167, 0, 0);
        this.player.sendMessage("Teleporting you to speed bridge location: " + speedBridgeLocation.toVector());
        this.player.teleport(speedBridgeLocation);
    } 
    
    private void resetBridgeArea() {
        BlockVector a = new BlockVector(165, 92, -166);
        BlockVector b = new BlockVector(159, 92, -157);
        BlockVector start = new BlockVector(Math.min(a.getBlockX(), b.getBlockX()), Math.min(a.getBlockY(), b.getBlockY()), Math.min(a.getBlockZ(), b.getBlockZ()));
        BlockVector end = new BlockVector(Math.max(a.getBlockX(), b.getBlockX()), Math.max(a.getBlockY(), b.getBlockY()), Math.max(a.getBlockZ(), b.getBlockZ()));
        
        this.player.sendMessage("Resetting bridge area from " + a + " to " + b + ".");
        for (int x = start.getBlockX(); x <= end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y <= end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z <= end.getBlockZ(); z++) {
                    this.player.getWorld().getBlockAt(new Location(this.player.getWorld(), x, y, z)).setType(Material.AIR);
                }
            }
        }
    }
    
    @Override
    public void stop() {
        resetBridgeArea();
        this.isActive = false;
    }

    public int getHeightLimit() {
        return this.heightLimit;
    }

    public void onFallBelowHeight() {
        teleportPlayerToStart();
        resetBridgeArea();
    }
    
    public boolean playerHasFallen() {
        return this.player.getLocation().getBlockY() < this.heightLimit;
    }
    
    public boolean playerIsPastFinishLine() {
        return this.player.getLocation().getBlockZ() > this.finishLine;
    }

    public void onSuccess() {
        this.player.sendMessage("Success!");
        stop();
    }
}
