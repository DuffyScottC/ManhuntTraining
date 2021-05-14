package me.braekpo1nt.commands;

import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CraftingCommand implements CommandExecutor {

    private Main plugin;

    public CraftingCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("crafting").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        Player player = (Player) sender;
        Material[] mats = Material.values();
        Random rand = new Random();
        
        Material goalMat = mats[rand.nextInt(mats.length)];
        Recipe goalRecipe = Bukkit.getRecipe(goalMat.getKey());
        while (!(goalRecipe instanceof ShapedRecipe)) {
            goalMat = mats[rand.nextInt(mats.length)];
            goalRecipe = Bukkit.getRecipe(goalMat.getKey());
        }
        
        ShapedRecipe shaped = (ShapedRecipe) goalRecipe;
        List<ItemStack> ingredients = new ArrayList<>();
        ingredients.addAll(shaped.getIngredientMap().values());
        
        givePlayerIngredients(player, ingredients);
        player.sendMessage("You must craft a " + goalMat.name());
        
        
        return true;
    }

    /**
     * Gives the list of ingredients to the player
     * @param player The player to give the ingredients to
     * @param ingredients The ingredients to give to the player
     */
    public void givePlayerIngredients(Player player, List<ItemStack> ingredients) {
        for (ItemStack ingredient : ingredients) {
            player.getInventory().addItem(ingredient);
        }
    }

}
