package me.braekpo1nt.commands.activities.speedbridge;

import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedBridgeActivity implements Activity {
    
    private int heightLimit = 92;
    private int finishLine = -157;
    private Player player;
    /**
     * True if this activity is active, false if not.
     */
    private boolean isActive = false;
    /**
     * The location the player should start at
     */
    private BlockVector startLocation = new BlockVector(162, 93, -167);
    private BoundingBox bridgeArea;
    
    
    public SpeedBridgeActivity(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SpeedBridgeListener(this), plugin);
        
        BlockVector start = new BlockVector(165, 92, -166);
        BlockVector end = new BlockVector(159, 92, -157);
        this.bridgeArea = new BoundingBox(start.getBlockX(), start.getBlockY(), start.getBlockZ(), end.getBlockX(), end.getBlockY(), end.getBlockZ());
    }
    
    @Override
    public void start(Player player) {
        this.player = player;
        teleportPlayerToStart();
        resetBridgeArea();
        clearPlayersInventory();
        givePlayerBridgeBlocks();
        this.isActive = true;
        this.player.sendMessage("Start speed bridging!");
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void stop() {
        resetBridgeArea();
        clearPlayersInventory();
        this.isActive = false;
    }

    @Override
    public boolean configure(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("buildarea")) {
                if (args.length == 7) {
                    int[] coords = new int[6];
                    for (int i = 1; i < 7; i++) {
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
                                coords[i-1] = getRelativeCoordinate(playerSender, offset, (i-1) % 3);
                            }
                        } else {
                            try {
                               coords[i-1] =  Integer.parseInt(args[i]);
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
                    
                } else {
                    sender.sendMessage("Must provide two valid block locations.");
                    return false;
                }
            } else {
                sender.sendMessage("Provide a valid option. \"" + args[0] + "\" is not a recognized option.");
                return false;
            }
        } else {
            sender.sendMessage("Please provide a valid option argument.");
            return false;
        }
        return false;
    }

    @Override
    public List<String> onConfigureTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("buildarea");
            return options;
        } else if (args.length > 1) {
            if (args[0].equals("buildarea")) {
                if (2 <= args.length && args.length <= 4) {
                    return Arrays.asList(getCoordTabComplete(sender, args.length - 2));
                } else if (5 <= args.length && args.length <= 7) {
                    return Arrays.asList(getCoordTabComplete(sender, args.length - 5));
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

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
    private int getRelativeCoordinate(Player playerSender, double offset, int argPosition) {
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
     * @param sender The sender of the command
     * @param argPosition The position of the coordinate component to return
     * @return x, y, or z coordinate if the argPosition is 0, 1, or 2 respectively.
     * If the sender is not a Player, the sender is not looking at a block in a range 
     * of 10 blocks, or the argPosition is not 0, 1, or 2, 
     * "~" is returned.
     */
    private String getCoordTabComplete(CommandSender sender, int argPosition) {
        if (!(sender instanceof Player)) {
            return "0";
        }
        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlockExact(10);
        // if the player is not targeting a block
        if (targetBlock == null) {
            return "~";
        } else {
            // if the player is targeting a block
            switch (argPosition) {
                case 0:
                    return Integer.toString(targetBlock.getLocation().getBlockX());
                case 1:
                    return Integer.toString(targetBlock.getLocation().getBlockY());
                case 2:
                    return Integer.toString(targetBlock.getLocation().getBlockZ());
                default:
                    return "~";
            }
        }
    }

    private void teleportPlayerToStart() {
        this.player.sendMessage("Teleporting you to speed bridge location: " + startLocation);
        this.player.teleport(startLocation.toLocation(this.player.getWorld()));
    }
    
    private void resetBridgeArea() {
        this.player.sendMessage("Resetting bridge area.");
        for (int x = bridgeArea.getMin().getBlockX(); x <= bridgeArea.getMax().getBlockX(); x++) {
            for (int y = bridgeArea.getMin().getBlockY(); y <= bridgeArea.getMax().getBlockY(); y++) {
                for (int z = bridgeArea.getMin().getBlockZ(); z <= bridgeArea.getMax().getBlockZ(); z++) {
                    this.player.getWorld().getBlockAt(new Location(this.player.getWorld(), x, y, z)).setType(Material.AIR);
                }
            }
        }
    }

    private void clearPlayersInventory() {
        this.player.getInventory().clear();
    }

    private void givePlayerBridgeBlocks() {
        this.player.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 64));
    }
    
    private void resetPlayersInventory() {
        clearPlayersInventory();
        givePlayerBridgeBlocks();
    }

    public void onFallBelowHeight() {
        teleportPlayerToStart();
        resetBridgeArea();
        resetPlayersInventory();
    }
    
    public boolean playerHasFallen() {
        return this.player.getLocation().getBlockY() < this.heightLimit;
    }
    
    public boolean playerIsPastFinishLine() {
        return this.player.getLocation().getBlockZ() > this.finishLine;
    }

    public void onSuccess() {
        this.player.sendMessage("Success!");
        stop();
    }
}
