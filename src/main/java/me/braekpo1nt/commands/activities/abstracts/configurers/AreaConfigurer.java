package me.braekpo1nt.commands.activities.abstracts.configurers;

import me.braekpo1nt.commands.activities.interfaces.ActivityConfigurer;
import me.braekpo1nt.commands.activities.interfaces.Confirmable;
import me.braekpo1nt.manhunttraining.Main;
import me.braekpo1nt.utils.Utils;
import me.braekpo1nt.visualizers.BoundingBoxVisualizer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.util.Arrays;
import java.util.List;

public abstract class AreaConfigurer implements ActivityConfigurer, Confirmable {
    
    protected Main plugin;
    
    //=============
    // Confirmable
    //=============
    private final Material confirmMat = Material.GREEN_DYE;
    private final Material declineMat = Material.RED_DYE;
    /**
     * True when this configurer is waiting for player confirmation
     * of the chosen area
     */
    private boolean confirming = false;
    /**
     * Displays the selected area to the player and allows them to
     * visualize the area before confirming their choice.
     */
    private final BoundingBoxVisualizer boundingBoxVisualizer;
    /**
     * Holds the area to be displayed to the user for confirmation
     * then saved as the new area.
     */
    private BoundingBox area;
    /**
     * The player's inventory to be stored while the player
     * confirming their selection
     */
    private ItemStack[] inventoryContents;

    public AreaConfigurer(Main plugin) {
        new ConfirmationListener(this);
        this.plugin = plugin;
        this.boundingBoxVisualizer = new BoundingBoxVisualizer(plugin);
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        // if the confirmation process is already in order
        if (confirming) {
            // don't let us confirm again
            sender.sendMessage("Please confirm or decline before running this again.");
            return false;
        }
        
        if (args.length == 6) {
            BlockVector start = Utils.createBlockVectorFromArgs(sender, Arrays.copyOfRange(args, 0, 3));
            BlockVector end = Utils.createBlockVectorFromArgs(sender, Arrays.copyOfRange(args, 3, 6));
            if (start == null || end == null) {
                sender.sendMessage("Must provide two valid block locations.");
                return false;
            }

            BoundingBox newArea = new BoundingBox(start.getBlockX(), start.getBlockY(), start.getBlockZ(), end.getBlockX(), end.getBlockY(), end.getBlockZ());
            
            this.area = newArea;
            if (sender instanceof Player) {
                initiateConfirm((Player) sender);
            } else {
                confirm();
            }
            
            return true;

        } else {
            sender.sendMessage("Must provide two valid block locations.");
            return false;
        }
    }
    
    protected void setArea(BoundingBox area) {
        plugin.getConfig().set(this.getConfigString(), area);
        plugin.saveConfig();
    }

    /**
     * This returns the string used to save the area to the
     * config file.
     * @return The string used to save the area in the config file.
     */
    protected abstract String getConfigString();

    /**
     * Returns a tab complete list of one String representing the x or z
     * coordinate of the block the player is looking at, according to which
     * argument number they are on. Relative coordinates if the player is
     * not looking at a block in 10 blocks.
     * @param sender the sender
     * @param command the command
     * @param label the label
     * @param args the args to perform tab completion on
     * @return a list of one string with the x or z component of the 
     * location of the block the player is looking at according to the 
     * argument position. null if the sender is not a player. Relative 
     * coordinates if the player is not looking at a block in 10 blocks.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return Utils.onTargetBlockTabComplete(player, (args.length - 1) % 3);
        } else {
            return null;
        }
    }
    
    //============
    // Confirmable
    //============
    
    private void initiateConfirm(Player player) {
        
        inventoryContents = player.getInventory().getContents();
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(confirmMat), new ItemStack(declineMat));
        player.sendMessage("Confirm the area...");
        confirming = true;
        boundingBoxVisualizer.setBoundingBox(this.area);
        boundingBoxVisualizer.show(player);
    }
    
    private void restoreInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().addItem(inventoryContents);
    }
    
    @Override
    public void confirm() {
        
    }
    
    @Override
    public void decline() {
        
    }
    
    @Override
    public boolean isConfirming() {
        return confirming;
    }
    
}
