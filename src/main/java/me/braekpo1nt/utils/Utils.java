package me.braekpo1nt.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class Utils {
    
    /**
     * Returns the relative coordinate position of the given Player given
     * an offset. Returns x, y, or z if argPosition is 0, 1, or 2, respectively.
     *
     * @param playerSender
     * @param argPosition
     * @return The x, y, or z coordinate of the player plus the given offset (rounded
     * to an int) if the argPosition is 0, 1, or 2, respectively. If the argPosition is
     * not 0, 1, or 2, returns the offset rounded to the nearest int. 
     */
    public static int getRelativeCoordinate(Player playerSender, double offset, int argPosition) {
        Location loc = playerSender.getLocation();
        int intOffset = (int) Math.round(offset);
        switch (argPosition) {
            case 0:
                return loc.getBlockX() + intOffset;
            case 1:
                return loc.getBlockY() + intOffset;
            case 2:
                return loc.getBlockZ() + intOffset;
            default:
                return intOffset;
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
    
}
