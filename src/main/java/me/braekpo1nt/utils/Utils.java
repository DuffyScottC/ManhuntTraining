package me.braekpo1nt.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.util.Arrays;
import java.util.List;

public abstract class Utils {
    
    /**
     * Returns the relative coordinate position of the given Player given
     * an offset. Returns x, y, or z if argPosition is 0, 1, or 2, respectively.
     *
     * @param referenceLocation The location to get the coordinates relative to
     * @param argPosition The position of the argument. Indicates that you want
     *                    x, y, or z coordinate given 0, 1, or 2, respectively.
     * @return The x, y, or z coordinate of the player plus the given 
     * offset if the argPosition is 0, 1, or 2, respectively. 
     * If the argPosition is not 0, 1, or 2, returns the offset 
     * rounded to the nearest int. 
     */
    public static double getRelativeCoordinate(Location referenceLocation, double offset, int argPosition) {
        switch (argPosition) {
            case 0:
                return referenceLocation.getX() + offset;
            case 1:
                return referenceLocation.getY() + offset;
            case 2:
                return referenceLocation.getZ() + offset;
            default:
                return offset;
        }
    }
    
    /**
     * Returns returns the x, y, or z coordinate of the block the sender
     * is looking at if the argPosition is 0, 1, or 2 respectively.
     * If the sender is not looking at a block, returns "~".
     * If the sender is not a player, will return "0".
     * @param player The player who sent the command, and who should be
     *               looking at a target block.
     * @param argPosition The position of the coordinate component to return
     * @return A List of one String element: the x, y, or z coordinate if the argPosition is 0, 1, or 2
     * respectively. If the sender is not a Player, the sender is not looking at a block in a range 
     * of 10 blocks, or the argPosition is not 0, 1, or 2, 
     * "~" is returned.
     */
    public static List<String> onTargetBlockTabComplete(Player player, int argPosition) {
        Block targetBlock = player.getTargetBlockExact(10);
        // if the player is not targeting a block
        if (targetBlock == null) {
            return Arrays.asList("~");
        } else {
            // if the player is targeting a block
            switch (argPosition) {
                case 0:
                    return Arrays.asList(Integer.toString(targetBlock.getLocation().getBlockX()));
                case 1:
                    return Arrays.asList(Integer.toString(targetBlock.getLocation().getBlockY()));
                case 2:
                    return Arrays.asList(Integer.toString(targetBlock.getLocation().getBlockZ()));
                default:
                    return Arrays.asList("~");
            }
        }
    }
    
    public static BlockVector createBlockVectorFromArgs(CommandSender sender, String[] args) {
        int[] coords = new int[3];
        for (int i = 0; i < 3; i++) {
            if (args[i].matches("^~-?\\d*\\.?\\d*$")) {
                double offset = 0.0;
                if (args[i].length() > 1) {
                    try {
                        String offsetString = args[i].substring(1);
                        offset = Double.parseDouble(offsetString);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage("\"" + args[i] + "\" is not a valid relative coordinate component.");
                        return null;
                    }
                }
                if (sender instanceof Player) {
                    Player playerSender = (Player) sender;
                    double coord = getRelativeCoordinate(playerSender.getLocation(), offset, i % 3);
                    coords[i] = Math.round((float) coord);
                }
            } else {
                try {
                    coords[i] =  Math.round(Float.parseFloat(args[i]));
                } catch (NumberFormatException ex) {
                    sender.sendMessage("\"" + args[i] + "\" is not a valid coordinate component.");
                    return null;
                }
            }
        }
        return new BlockVector(coords[0] + .5, coords[1], coords[2] + .5);
    }
    
}
