package me.braekpo1nt.commands.activities.abstracts.configurers;

import me.braekpo1nt.commands.activities.interfaces.Confirmable;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Accepts a {@link Confirmable}. Listens for a player clicking confirm or decline,
 * then calls the appropriate function of the given Confirmable. 
 */
public class ConfirmationListener implements Listener {
    
    private final Confirmable confirmable;
    
    private final Material confirmMat;
    private final Material declineMat;
    
    /**
     * Instantiates a new ConfirmationListener using the given confirm and decline material.
     * @param confirmable The confirmable this should call the confirm or decline functions
     *                    of on click event
     * @param confirmMat The material that should indicate a confirm command
     * @param declineMat The material that should indicate a decline command
     */
    public ConfirmationListener(Confirmable confirmable, Material confirmMat, Material declineMat) {
        this.confirmable = confirmable;
        this.confirmMat = confirmMat;
        this.declineMat = declineMat;
    }
    
    @EventHandler
    public void clickListener(PlayerInteractEvent event) {
        event.getPlayer().sendMessage("clickListener");
        if (confirmable.isConfirming()) {
            Material mat = event.getItem().getType();
            if (mat == confirmMat) {
                event.setCancelled(true);
                confirmable.confirm();
            } else if (mat == declineMat) {
                event.setCancelled(true);
                confirmable.decline();
            }
        }
    }
    
}
