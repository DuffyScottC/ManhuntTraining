package me.braekpo1nt.commands.activities.speedbridge.configurers;

import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.commands.interfaces.ActivityConfigurer;
import me.braekpo1nt.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BridgeAreaConfigurer implements ActivityConfigurer {
    
    private SpeedBridgeActivity speedBridgeActivity;
    
    public BridgeAreaConfigurer(SpeedBridgeActivity speedBridgeActivity) {
        this.speedBridgeActivity = speedBridgeActivity;
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 6) {
            int[] coords = new int[6];
            for (int i = 0; i < 6; i++) {
                if (args[i].matches("^~-?\\d*\\.?\\d*$")) {
                    double offset = 0.0;
                    if (args[i].length() > 1) {
                        try {
                            String offsetString = args[i].substring(1);
                            offset = Double.parseDouble(offsetString);
                        } catch (NumberFormatException ex) {
                            sender.sendMessage("Must provide two valid block locations. \"" + args[i] + "\" is not a valid coordinate.");
                            return false;
                        }
                    }
                    if (sender instanceof Player) {
                        Player playerSender = (Player) sender;
                        coords[i] = Utils.getRelativeCoordinate(playerSender, offset, i % 3);
                    }
                } else {
                    try {
                        coords[i] =  Integer.parseInt(args[i]);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage("Must provide two valid block locations. \"" + args[i] + "\" is not a valid coordinate.");
                        return false;
                    }
                }
            }

            StringBuilder s = new StringBuilder();
            for (int coord : coords) {
                s.append(coord);
                s.append(",");
            }
            sender.sendMessage(s.toString());
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
