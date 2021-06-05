package me.braekpo1nt.commands.activities.abstracts.configurers;

import me.braekpo1nt.commands.interfaces.Confirmable;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ConfirmationListener implements Listener {
    
    private final Confirmable confirmable;
    
    private final Material confirmMat = Material.GREEN_WOOL;
    private final Material declineMat = Material.RED_WOOL;
    
    public ConfirmationListener(Confirmable confirmable) {
        this.confirmable = confirmable;
    }
    
    public void clickListener(PlayerInteractEvent event) {
        if (confirmable.isActive()) {
            Material mat = event.getItem().getType();
            if (mat == confirmMat) {
                confirmable.confirm();
            } else if (mat == declineMat) {
                confirmable.decline();
            }
        }
    }
    
}
