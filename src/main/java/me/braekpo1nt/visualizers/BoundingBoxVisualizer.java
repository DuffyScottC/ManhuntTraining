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
    private final double INCREMENT = 1;
    
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
                    double maxX = boundingBox.getMaxX();
                    double maxY = boundingBox.getMaxY();
                    double maxZ = boundingBox.getMaxZ();
                    double minX = boundingBox.getMinX();
                    double minY = boundingBox.getMinY();
                    double minZ = boundingBox.getMinZ();
                    for (double x = minX; x <= maxX; x += INCREMENT) {
                        for (double y = minY; y <= maxY; y += INCREMENT) {
                            for (double z = minZ; z <= maxZ; z += INCREMENT) {
                                int count = 0;

                                if (x == minX || x == maxX) {
                                    count++;
                                }

                                if (y == minY || y == maxY) {
                                    count++;
                                }

                                if (z == minZ || z == maxZ) {
                                    count++;
                                }

                                if (count == 2 || count == 3) {
                                    Location loc = new Location(player.getWorld(), x+.5, y+.5, z+.5);
                                    player.spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1));
                                }
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
