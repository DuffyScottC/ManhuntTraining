package me.braekpo1nt.commands.activities.abstracts.configurers;

import me.braekpo1nt.commands.activities.interfaces.ActivityConfigurer;
import me.braekpo1nt.manhunttraining.Main;
import me.braekpo1nt.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.util.Arrays;
import java.util.List;

public abstract class AreaConfigurer implements ActivityConfigurer {
    
    protected Main plugin;
    
    public AreaConfigurer(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 6) {
            BlockVector start = Utils.createBlockVectorFromArgs(sender, Arrays.copyOfRange(args, 0, 3));
            BlockVector end = Utils.createBlockVectorFromArgs(sender, Arrays.copyOfRange(args, 3, 6));
            if (start == null || end == null) {
                sender.sendMessage("Must provide two valid block locations.");
                return false;
            }

            BoundingBox bridgeArea = new BoundingBox(start.getBlockX(), start.getBlockY(), start.getBlockZ(), end.getBlockX(), end.getBlockY(), end.getBlockZ());
            
            this.setArea(bridgeArea);

            sender.sendMessage("Area set.");
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
    
}
