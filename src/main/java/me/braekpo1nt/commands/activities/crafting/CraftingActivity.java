package me.braekpo1nt.commands.activities.crafting;

import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CraftingActivity implements Activity {

    private Material goalType = null;
    private List<ItemStack> goalIngredients;
    private Player player;
    private final Main plugin;
    private long stopwatchStart;
    private boolean isActive;

    /**
     * Initializes the CraftingActivity. Includes registering event listeners.
     * @param plugin
     */
    public CraftingActivity(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new CraftingListener(this, plugin), plugin);
    }

    
    @Override
    public void start(Player player) {
        this.isActive = true;
        this.player = player;
        assignCraftingTask(player);
        startStopwatch();
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }


    private void startStopwatch() {
        this.stopwatchStart = System.currentTimeMillis();
    }
    
    private long stopStopwatch() {
        return System.currentTimeMillis() - this.stopwatchStart;
    }

    /**
     * Tells the player which item to craft, gives the player the required
     * items, and tells the craftingListener what item to watch for the
     * creation of and to start the timer.
     */
    private void assignCraftingTask(Player player) {
        Recipe goalRecipe = getGoalRecipe();
        goalIngredients = getIngredients(goalRecipe);
        givePlayerIngredients(player, goalIngredients);
        this.goalType = goalRecipe.getResult().getType();
        player.sendMessage("You must craft a " + goalRecipe.getResult().getType());
    }

    /**
     * Returns a new goal recipe for the player to
     * craft. 
     * @return the recipe the player should craft.
     */
    private Recipe getGoalRecipe() {
        Random rand = new Random();
        Material[] mats = Material.values();
        Material goalMat = mats[rand.nextInt(mats.length)];
        Recipe goalRecipe = Bukkit.getRecipe(goalMat.getKey());
        while (!(goalRecipe instanceof ShapedRecipe || goalRecipe instanceof ShapelessRecipe)) {
            goalMat = mats[rand.nextInt(mats.length)];
            goalRecipe = Bukkit.getRecipe(goalMat.getKey());
        }
        return goalRecipe;
    }

    /**
     * Returns the list of ingredients of the passed in recipe, provided
     * it is of type ShapedRecipe or ShapelessRecipe. If it is not
     * one of those two types, returns an empty list.
     * @param recipe
     * @return
     */
    private List<ItemStack> getIngredients(Recipe recipe) {
        List<ItemStack> ingredients = new ArrayList<>();
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shaped = (ShapedRecipe) recipe;
            for (ItemStack ingredient : shaped.getIngredientMap().values()) {
                if (ingredient != null) {
                    ingredients.add(ingredient);
                }
            }
        } else if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
            ingredients.addAll(shapeless.getIngredientList());
        }
        return ingredients;
    }
    
    /**
     * Gives the list of ingredients to the player
     * @param player The InventoryHolder to give the ingredients to
     * @param ingredients The ingredients to give to the player
     */
    public void givePlayerIngredients(Player player, List<ItemStack> ingredients) {
        for (ItemStack ingredient : ingredients) {
            player.getInventory().addItem(ingredient);
        }
    }
    
    public void craftSuccess() {
        this.player.sendMessage("Success! " + Double.toString((double) stopStopwatch() / 1000.0) + " seconds.");
        this.goalType = null;
        Bukkit.getScheduler().scheduleSyncDelayedTask(CraftingActivity.this.plugin, new Runnable() {
            @Override
            public void run() {
                CraftingActivity.this.player.getInventory().clear();
            }
        }, 1L);
        this.stopwatchStart = System.currentTimeMillis();
        
    }
    
    @Override
    public void stop() {
        this.player.getInventory().clear();
        this.isActive = false;
    }

    public Material getGoalType() {
        return this.goalType;
    }
    
    public List<ItemStack> getGoalIngredients() {
        return this.goalIngredients;
    }
}
