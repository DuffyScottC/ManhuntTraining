package me.braekpo1nt.commands.activities.speedbridge.configurers;

import me.braekpo1nt.commands.activities.speedbridge.SpeedBridgeActivity;
import me.braekpo1nt.commands.interfaces.ActivityConfigurer;
import me.braekpo1nt.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;

import java.util.Arrays;
import java.util.List;

public class BridgeStartLocationConfigurer implements ActivityConfigurer {
    
    SpeedBridgeActivity speedBridgeActivity;
    
    public BridgeStartLocationConfigurer(SpeedBridgeActivity speedBridgeActivity) {
        this.speedBridgeActivity = speedBridgeActivity;
    }
    
    @Override
    public boolean onConfigure(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getLogger().info("args.length: " + args.length);
        if (args.length == 3) {
            BlockVector startLocation = Utils.createBlockVectorFromArgs(sender, args);
            if (startLocation == null) {
                sender.sendMessage("Please provide a valid location.");
                return false;
            }
            speedBridgeActivity.setStartLocation(startLocation);
            sender.sendMessage("Start location set.");
            return true;
        } else {
            sender.sendMessage("Please provide a valid location.");
            return false;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (1 <= args.length && args.length <= 3) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                return Utils.onTargetBlockTabComplete(player, (args.length - 1) % 3);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}