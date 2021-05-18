package me.braekpo1nt.commands.activities.speedbridge;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedBridgeListener implements Listener {
    
    private final SpeedBridgeActivity speedBridgeActivity;
    
    public SpeedBridgeListener(SpeedBridgeActivity speedBridgeActivity) {
        this.speedBridgeActivity = speedBridgeActivity;
    }
    
    @EventHandler
    public void fallListener(PlayerMoveEvent event) {
        if (speedBridgeActivity.isSpeedBridging()) {
            if (speedBridgeActivity.playerHasFallen()) {
                speedBridgeActivity.onFallBelowHeight();
                return;
            } else if (speedBridgeActivity.playerIsPastFinishLine()) {
                speedBridgeActivity.onSuccess();
            }
            
        }
    }
    
}
