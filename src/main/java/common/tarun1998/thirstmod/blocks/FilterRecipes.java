package tarun1998.thirstmod.blocks;

import java.util.HashMap;
import java.util.Map;

import tarun1998.thirstmod.DrinkLoader;
import net.minecraft.src.*;

public class FilterRecipes {
    private static Map<Integer, ItemStack> recipes = new HashMap<Integer, ItemStack>();
    private static FilterRecipes instance;

    public FilterRecipes() {
        addRecipe(Item.bucketWater.shiftedIndex, new ItemStack(DrinkLoader.fBucket));
    }

    public static FilterRecipes getInstance() {
        if (instance == null) {
            instance = new FilterRecipes();
            return instance;
        }
        return instance;
    }

    public void addRecipe(int i, ItemStack stack) {
        recipes.put(i, stack);
    }

    public HashMap<Integer, ItemStack> getRecipes() {
        return (HashMap<Integer, ItemStack>) recipes;
    }
}
