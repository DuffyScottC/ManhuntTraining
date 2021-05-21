package me.braekpo1nt.commands.activities.abstracts.configurers;

import me.braekpo1nt.commands.interfaces.ActivityConfigurer;
import me.braekpo1nt.manhunttraining.Main;
import me.braekpo1nt.utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.util.List;

public abstract class ChunkConfigurer implements ActivityConfigurer {
    
    protected Main plugin;
    
    public ChunkConfigurer(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Chunk chunk = player.getLocation().getChunk();
                setChunk(chunk);
                sender.sendMessage("Chunk is set.");
                return true;
            } else {
                sender.sendMessage("Please provide valid chunk coordinates.");
                return false;
            }
        } else {
            if (args.length == 2) {
                String[] newArgs = new String[] {args[0], "0", args[1]};
                BlockVector chunkVector = Utils.createBlockVectorFromArgs(sender, newArgs);
                if (chunkVector != null) {
                    Chunk chunk;
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        chunk = player.getWorld().getChunkAt(chunkVector.getBlockX(), chunkVector.getBlockZ());
                    } else {
                        chunk = sender.getServer().getWorlds().get(0).getChunkAt(chunkVector.getBlockX(), chunkVector.getBlockZ());
                    }
                    setChunk(chunk);
                    sender.sendMessage("Chunk is set.");
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

    private void setChunk(Chunk chunk) {
        plugin.getConfig().set(this.getConfigString(), chunk);
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
    
}
