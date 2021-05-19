package me.braekpo1nt.commands.activities.speedbridge.configurers;

import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.commands.interfaces.ActivityConfigurer;
import me.braekpo1nt.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.util.Arrays;
import java.util.List;

public class BridgeAreaConfigurer implements ActivityConfigurer {
    
    private SpeedBridgeActivity speedBridgeActivity;
    
    public BridgeAreaConfigurer(SpeedBridgeActivity speedBridgeActivity) {
        this.speedBridgeActivity = speedBridgeActivity;
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 6) {
            BlockVector start = Utils.createBlockVectorFromArgs(sender, Arrays.copyOfRange(args, 0, 3));
            BlockVector end = Utils.createBlockVectorFromArgs(sender, Arrays.copyOfRange(args, 3, 6));
            if (start == null || end == null) {
                sender.sendMessage("Please provide a valid block location");
                return false;
            }
            speedBridgeActivity.setBridgeArea(start, end);
            sender.sendMessage("Bridge area set.");
            return true;

        } else {
            sender.sendMessage("Must provide two valid block locations.");
            return false;
        }
    }
    
    /**
     * Returns a tab complete list of one String representing the x, y, or z
     * coordinate of the block the player is looking at, according to which
     * argument number they are on.
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return a list of one string with the x, y,. or z component of the 
     * location of the block the player is looking at according to the 
     * argument position. null if the sender is not a player.
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
