package net.minecraft.src.ThirstMod.containers;

import java.util.HashMap;
import net.minecraft.src.*;
import java.util.Map;

public class WaterCollectorRecipes
{
    private static final WaterCollectorRecipes solidifyingBase = new WaterCollectorRecipes();
    private Map solidifyingList;

    public static final WaterCollectorRecipes fill()
    {
        return solidifyingBase;
    }

    private WaterCollectorRecipes()
    {
        solidifyingList = new HashMap();
    }

    public void addRecipe(int i, ItemStack itemstack)
    {
        solidifyingList.put(Integer.valueOf(i), itemstack);
    }

    public ItemStack getSolidifyingResult(int i)
    {
        return (ItemStack)solidifyingList.get(Integer.valueOf(i));
    }

    public Map getSolidifyingList()
    {
        return solidifyingList;
    }
}
