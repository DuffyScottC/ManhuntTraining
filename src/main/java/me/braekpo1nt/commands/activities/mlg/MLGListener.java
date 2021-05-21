package me.braekpo1nt.commands.activities.mlg;

import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MLGListener implements Listener {

    private final MLGActivity mlgActivity;
    private final Main plugin;
    
    public MLGListener(MLGActivity mlgActivity, Main plugin) {
        this.mlgActivity = mlgActivity;
        this.plugin = plugin;
    }
    
    @EventHandler
    public void deathListener(EntityDamageEvent event) {
        if (mlgActivity.isActive()) {
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
                Player player = (Player) event.getEntity();
                mlgActivity.retry();
            }
        }
    }
    
    @EventHandler
    public void fallListener(PlayerMoveEvent event) {
        if (mlgActivity.isActive()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (mlgActivity.playerIsOnGround()) {
                        mlgActivity.onSuccess();
                    }
                }
            }, 1L);
        }
    }
    
}
