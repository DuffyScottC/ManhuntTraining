package me.braekpo1nt.commands.activities.abstracts.configurers;

import me.braekpo1nt.commands.activities.interfaces.ActivityConfigurer;
import me.braekpo1nt.commands.activities.interfaces.Confirmable;
import me.braekpo1nt.manhunttraining.Main;
import me.braekpo1nt.utils.Utils;
import me.braekpo1nt.visualizers.BoundingBoxVisualizer;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.util.List;

public abstract class ChunkConfigurer implements ActivityConfigurer, Confirmable {
    
    protected Main plugin;

    //=============
    // Confirmable
    //=============
    // ------------------common
    private final Material declineMat = Material.RED_DYE;
    private final Material confirmMat = Material.GREEN_DYE;
    /**
     * True when this configurer is waiting for player confirmation
     * of the chosen area
     */
    private boolean confirming = false;
    /**
     * The player's inventory to be stored while the player
     * confirming their selection
     */
    private ItemStack[] inventoryContents;
    /**
     * The player who is confirming the area.
     */
    private Player player;
    // ------------------unique
    /**
     * Displays the selected area to the player and allows them to
     * visualize the area before confirming their choice.
     */
    private final BoundingBoxVisualizer boundingBoxVisualizer;
    /**
     * Holds the x and z of the chunk to be displayed to the 
     * user for confirmation then saved as the new chunk.
     */
    private BlockVector chunkVector;

    public ChunkConfigurer(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ConfirmationListener(this, confirmMat, declineMat), plugin);
        this.plugin = plugin;
        this.boundingBoxVisualizer = new BoundingBoxVisualizer(plugin);
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Chunk chunk = player.getLocation().getChunk();
                this.chunkVector = new BlockVector(chunk.getX(), 0, chunk.getZ());
                
                if (sender instanceof Player) {
                    player = (Player) sender;
                    initiateConfirm();
                } else {
                    confirm();
                }
                
                return true;
            } else {
                sender.sendMessage("Please provide valid chunk coordinates.");
                return false;
            }
        } else {
            if (args.length == 2) {
                String[] newArgs = new String[] {args[0], "0", args[1]};
                BlockVector blockInChunk = Utils.createBlockVectorFromArgs(sender, newArgs);
                if (blockInChunk != null) {
                    Chunk chunk;
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        chunk = player.getWorld().getChunkAt(blockInChunk.getBlockX(), blockInChunk.getBlockZ());
                    } else {
                        chunk = sender.getServer().getWorlds().get(0).getChunkAt(blockInChunk.getBlockX(), blockInChunk.getBlockZ());
                    }
                    this.chunkVector = new BlockVector(chunk.getX(), 0, chunk.getZ());
                    
                    if (sender instanceof Player) {
                        player = (Player) sender;
                        initiateConfirm();
                    } else {
                        confirm();
                    }
                    
                    return true;
                } else {
                    return false;
                }
            } else {
                sender.sendMessage("Please provide valid chunk coordinates.");
                return false;
            }
        }
    }

    protected void setChunkVector() {
        plugin.getConfig().set(this.getConfigString(), this.chunkVector);
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
            if (args.length == 1) {
                return Utils.onTargetBlockTabComplete(player, 0);
            } else if (args.length == 2) {
                return Utils.onTargetBlockTabComplete(player, 2);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    //============
    // Confirmable
    //============

    /**
     * Initializes everything for the confirm
     */
    private void initiateConfirm() {
        saveAndReplaceInventory();
        player.sendMessage("Confirm the area...");
        confirming = true;
        BoundingBox area = new BoundingBox(this.chunkVector.getBlockX(), 0, this.chunkVector.getBlockZ(), this.chunkVector.getBlockX()+16, player.getWorld().getMaxHeight(), this.chunkVector.getBlockZ()+16);
        boundingBoxVisualizer.setBoundingBox(area);
        boundingBoxVisualizer.show(player);
    }

    /**
     * Resets everything that was set for the confirmation
     */
    private void tearDownConfirm() {
        boundingBoxVisualizer.hide();
        restoreInventory();
        chunkVector = null;
        confirming = false;
        player = null;
    }

    /**
     * Saves the inventory of the player, then
     * replaces it with the confirm and decline items.
     */
    private void saveAndReplaceInventory() {
        inventoryContents = player.getInventory().getContents();
        player.getInventory().clear();
        player.getInventory().addItem(new ItemStack(confirmMat), new ItemStack(declineMat));
    }

    /**
     * Returns the player's inventory to the way it was
     * before the Confirmation (when we replaced it with
     * the confirm or decline items). 
     */
    private void restoreInventory() {
        player.getInventory().clear();
        player.getInventory().setContents(inventoryContents);
        inventoryContents = null;
    }

    @Override
    public void confirm() {
        player.sendMessage("Confirmed.");
        setChunkVector();
        tearDownConfirm();
    }

    @Override
    public void decline() {
        player.sendMessage("Decline.");
        tearDownConfirm();
    }

    @Override
    public boolean isConfirming() {
        return confirming;
    }
    
}
