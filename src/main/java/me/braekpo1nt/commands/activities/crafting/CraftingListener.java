package me.braekpo1nt.commands.activities.crafting;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CraftingListener implements Listener {
    
    private final CraftingActivity craftingActivity;
    
    public CraftingListener(CraftingActivity craftingActivity) {
        this.craftingActivity = craftingActivity;
    }
    
    @EventHandler
    public void craftingListener(CraftItemEvent event) {
        if (craftingActivity.getGoalType() != null) {
            if (event.getCurrentItem().equals(craftingActivity.getGoalType())) {
                event.getWhoClicked().sendMessage("Success!");
                craftingActivity.stop();
            } else {
                event.getWhoClicked().sendMessage("Wrong item crafted. Please try again to craft a " + craftingActivity.getGoalType());
            }
        }
    }
    
}
