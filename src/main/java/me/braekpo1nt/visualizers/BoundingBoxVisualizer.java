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
    private final double MARGIN = .001;
    
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

                                if (closeTo(x, minX) || closeTo(x, maxX)) {
                                    count++;
                                }

                                if (closeTo(y, minY) || closeTo(y, maxY)) {
                                    count++;
                                }

                                if (closeTo(z, minZ) || closeTo(z, maxZ)) {
                                    count++;
                                }

                                if (closeTo(count, 2) || closeTo(count, 3)) {
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

    /**
     * Returns true if a is withing a particular range of b.
     * The range is specified by a field.
     * @param a a double
     * @param b a double
     * @return True if a is close to b
     */
    private boolean closeTo(double a, double b) {
        return Math.abs(a - b) <= MARGIN;
    }
    
    public void hide() {
        Bukkit.getScheduler().cancelTask(particleTaskId);
    }
    
}
