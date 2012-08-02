/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.containers;

import java.util.HashMap;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.core.*;

import java.util.Map;

public class WaterCollectorRecipes {
	private static final WaterCollectorRecipes solidifyingBase = new WaterCollectorRecipes();
	private Map solidifyingList;

	public static final WaterCollectorRecipes fill() {
		return solidifyingBase;
	}

	private WaterCollectorRecipes() {
		solidifyingList = new HashMap();
		addRecipe(Item.glassBottle.shiftedIndex, new ItemStack(DrinkLoader.freshWater));
	}

	public void addRecipe(int i, ItemStack itemstack) {
		solidifyingList.put(Integer.valueOf(i), itemstack);
	}

	public ItemStack getSolidifyingResult(int i) {
		return (ItemStack) solidifyingList.get(Integer.valueOf(i));
	}

	public Map getSolidifyingList() {
		return solidifyingList;
	}
}
