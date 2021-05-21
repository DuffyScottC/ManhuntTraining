package me.braekpo1nt.visualizers;

import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ChunkVisualizer {

    private int particleTaskId;
    
    public void visualizeChunk(Chunk chunk, Player player, Main plugin) {
        //you must create a new particle every tick.
        particleTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if (count >= 10*20){
                    Bukkit.getScheduler().cancelTask(particleTaskId);
                } else {
                    player.spawnParticle(Particle.REDSTONE, player.getLocation().clone().add(new Vector(0, 1, 0)), 1, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1));
                }
                count += 1;
            }
        }, 0L, 1L);
    }
    
}
