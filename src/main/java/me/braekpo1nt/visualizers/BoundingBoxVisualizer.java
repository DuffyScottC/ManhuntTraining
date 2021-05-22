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
    private final int INCREMENT = 1;
    
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
                    int maxX = boundingBox.getMax().getBlockX();
                    int maxY = boundingBox.getMax().getBlockY();
                    int maxZ = boundingBox.getMax().getBlockZ();
                    int minX = boundingBox.getMin().getBlockX();
                    int minY = boundingBox.getMin().getBlockY();
                    int minZ = boundingBox.getMin().getBlockZ();
                    for (int x = minX; x <= maxX; x += INCREMENT) {
                        for (int y = minY; y <= maxY; y += INCREMENT) {
                            for (int z = minZ; z <= maxZ; z += INCREMENT) {
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
                                    Location loc = new Location(player.getWorld(), x, y, z);
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
