package me.braekpo1nt.visualizers;

import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class BoundingBoxVisualizer {

    private int particleTaskId;
    private final Main plugin;
    private final double INCREMENT = 1.0;
    
    private BoundingBox boundingBox;
    
    public BoundingBoxVisualizer(Main plugin) {
        this.plugin = plugin;
    }
    
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
    
    public void show(Player player) {
        //you must create a new particle every tick.
        particleTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (boundingBox != null) {
                    for (double x = boundingBox.getMinX(); x <= boundingBox.getMaxX(); x += INCREMENT) {
                        for (double y = boundingBox.getMinY(); y <= boundingBox.getMaxY(); y += INCREMENT) {
                            for (double z = boundingBox.getMinZ(); z <= boundingBox.getMaxZ(); z += INCREMENT) {
                                Location loc = new Location(player.getWorld(), x, y, z);
                                player.spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1));
                            }
                        }
                    }
                }
            }
        }, 0L, 1L);
    }
    
    public void hide() {
        Bukkit.getScheduler().cancelTask(particleTaskId);
    }
    
}
