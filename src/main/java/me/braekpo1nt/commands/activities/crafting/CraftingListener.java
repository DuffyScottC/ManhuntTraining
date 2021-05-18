package me.braekpo1nt.commands.activities.crafting;

import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CraftingListener implements Listener {
    
    private final CraftingActivity craftingActivity;
    private final Main plugin;
    
    public CraftingListener(CraftingActivity craftingActivity, Main plugin) {
        this.craftingActivity = craftingActivity;
        this.plugin = plugin;
    }
    
    @EventHandler
    public void craftingListener(CraftItemEvent event) {
        if (craftingActivity.getGoalType() != null) {
            if (event.getCurrentItem().getType().equals(craftingActivity.getGoalType())) {
                craftingActivity.craftSuccess();
            } else {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        event.getWhoClicked().getInventory().clear();
                    }
                }, 1L);
                giveIngredients(event.getWhoClicked(), craftingActivity.getGoalIngredients());
                event.getWhoClicked().sendMessage("Wrong item crafted. You crafted a " + event.getCurrentItem().getType() + ". Please try again to craft a " + craftingActivity.getGoalType());
            }
        }
    }
    
    
    
    private void giveIngredients(InventoryHolder inventoryHolder, List<ItemStack> ingredients) {
        for (ItemStack ingredient : ingredients) {
            inventoryHolder.getInventory().addItem(ingredient);
        }
    }
    
}
