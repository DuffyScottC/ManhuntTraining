package me.braekpo1nt.commands.activities.speedbridge;

import me.braekpo1nt.commands.activities.speedbridge.configurers.BridgeAreaConfigurer;
import me.braekpo1nt.commands.activities.speedbridge.configurers.BridgeStartLocationConfigurer;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import me.braekpo1nt.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.sql.Array;
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
    private BlockVector startLocation;
    private BoundingBox bridgeArea;
    
    
    public SpeedBridgeActivity(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(new SpeedBridgeListener(this), plugin);
        
        // Add new ActivityConfigurer objects here
        configurers.put("bridgearea", new BridgeAreaConfigurer(this));
        configurers.put("startlocation", new BridgeStartLocationConfigurer(this));
    }
    
    public void setBridgeArea(BlockVector start, BlockVector end) {
        this.bridgeArea = new BoundingBox(start.getBlockX(), start.getBlockY(), start.getBlockZ(), end.getBlockX(), end.getBlockY(), end.getBlockZ());
    }

    public void setStartLocation(BlockVector startLocation) {
        this.startLocation = startLocation;
    }
    
    @Override
    public void start(Player player) {
        if (startLocation == null) {
            player.sendMessage("Start location has not been set.");
            return;
        }
        if (bridgeArea == null) {
            player.sendMessage("Bridge area has not been set.");
            return;
        }
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
            if (configurers.containsKey(args[0])) {
                return configurers.get(args[0]).onConfigure(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            } else {
                sender.sendMessage("Provide a valid option. \"" + args[0] + "\" is not a recognized option.");
                return false;
            }
        } else {
            sender.sendMessage("Please provide a valid option argument.");
            return false;
        }
    }

    @Override
    public List<String> onConfigureTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(configurers.keySet());
        } else if (args.length > 1) {
            if (configurers.containsKey(args[0])) {
                return configurers.get(args[0]).onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void teleportPlayerToStart() {
        this.player.sendMessage("Teleporting you to speed bridge location: " + startLocation.toLocation(this.player.getWorld()));
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
