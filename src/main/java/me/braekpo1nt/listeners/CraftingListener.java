package me.braekpo1nt.listeners;

import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class CraftingListener implements Listener {
    
    @EventHandler
    public void craftingListener(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked(); 
        player.sendMessage("crafted a: " + event.getCurrentItem().getType());
    }
    
}
