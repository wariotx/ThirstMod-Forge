package net.minecraft.src.thirstmod.blocks;

import java.util.HashMap;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.DrinkLoader;

import java.util.Map;

public class RCRecipes {
	private static final RCRecipes solidifyingBase = new RCRecipes();
	private Map solidifyingList;

	public static final RCRecipes fill() {
		return solidifyingBase;
	}

	private RCRecipes() {
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
