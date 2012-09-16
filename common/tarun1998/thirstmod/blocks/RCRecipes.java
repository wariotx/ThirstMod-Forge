package tarun1998.thirstmod.blocks;

import java.util.HashMap;
import net.minecraft.src.*;
import java.util.Map;
import tarun1998.thirstmod.*;

public class RCRecipes {
	private static final RCRecipes solidifyingBase = new RCRecipes();
	private Map solidifyingList;

	public static final RCRecipes fill() {
		return solidifyingBase;
	}

	private RCRecipes() {
		solidifyingList = new HashMap();
		addRecipe(Item.glassBottle.shiftedIndex, new ItemStack(DrinkLoader.freshWater));
		if(ConfigHelper.wantFBucket == true) {
			addRecipe(Item.bucketEmpty.shiftedIndex, new ItemStack(ConfigHelper.fBucketId, 1, 0));
		}
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
