package me.braekpo1nt.commands.activities.crafting;

import me.braekpo1nt.commands.activities.crafting.configurers.CraftingStartLocationConfigurer;
import me.braekpo1nt.commands.activities.crafting.configurers.CraftingTableLocationConfigurer;
import me.braekpo1nt.commands.interfaces.Activity;
import me.braekpo1nt.commands.interfaces.ActivityConfigurer;
import me.braekpo1nt.manhunttraining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.*;

public class CraftingActivity implements Activity {

    public static final String START_LOCATION = "crafting.start-location";
    public static final String TABLE_LOCATION = "crafting.table-location";
    
    private Material goalType = null;
    private List<ItemStack> goalIngredients;
    private Player player;
    private final Main plugin;
    private long stopwatchStart;
    private boolean isActive;
    /**
     * The location the player will be teleported to when the craft activity is started.
     */
    private BlockVector startLocation;
    /**
     * The location the player will be teleported to look at
     */
    private BlockVector craftingTableLocation;

    /**
     * A map of {@link ActivityConfigurer}s for this {@link Activity}.
     * Maps configurer option names to their classes.
     * Add {@link ActivityConfigurer}s to this list to enable the
     * configuration of this activity.
     */
    private final Map<String, ActivityConfigurer> configurers = new HashMap<>();
    
    /**
     * Initializes the CraftingActivity. Includes registering event listeners.
     * @param plugin
     */
    public CraftingActivity(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new CraftingListener(this, plugin), plugin);
        
        configurers.put("startlocation", new CraftingStartLocationConfigurer(plugin));
        configurers.put("tablelocation", new CraftingTableLocationConfigurer(plugin));
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
        Bukkit.getLogger().info("Here2");
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
    
    @Override
    public void start(Player player) {
        Vector startLocationConf = plugin.getConfig().getVector(this.START_LOCATION);
        if (startLocationConf == null || !(startLocationConf instanceof BlockVector)) {
            player.sendMessage("Start location has not been set.");
            return;
        }
        this.startLocation = (BlockVector) startLocationConf;
        Vector tableLocationConf = plugin.getConfig().getVector(this.TABLE_LOCATION);
        if (tableLocationConf == null || !(tableLocationConf instanceof BlockVector)) {
            player.sendMessage("Crafting table location has not been set.");
            return;
        }
        this.craftingTableLocation = (BlockVector) tableLocationConf;
        this.isActive = true;
        this.player = player;
        teleportPlayerToStartPosition();
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
    
    private void teleportPlayerToStartPosition() {
        this.player.sendMessage("Teleporting you to crafting location: " + startLocation);
        Location loc = startLocation.toLocation(this.player.getWorld());
        Vector dir = craftingTableLocation.clone().subtract(startLocation);
        loc.setDirection(dir);
        this.player.teleport(loc);
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
