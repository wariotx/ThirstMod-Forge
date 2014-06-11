package tarun1998.thirstmod.blocks;

import java.util.HashMap;
import net.minecraft.src.*;
import java.util.Map;
import tarun1998.thirstmod.*;

public class RCRecipes {
	private static final RCRecipes solidifyingBase = new RCRecipes();
	private Map solidifyingList;
	private Map fillTime;

	public static final RCRecipes fill() {
		return solidifyingBase;
	}

	public RCRecipes() {
		solidifyingList = new HashMap();
		fillTime = new HashMap();
		addRecipe(Item.glassBottle.shiftedIndex, 200, new ItemStack(DrinkLoader.freshWater));
		addRecipe(DrinkLoader.woodGlass.shiftedIndex, 150, new ItemStack(DrinkLoader.woodWater));
		if (ConfigHelper.wantFBucket == true) {
			addRecipe(Item.bucketEmpty.shiftedIndex, 600, new ItemStack(DrinkLoader.fBucket));
		}
	}

	public void addRecipe(int i, int time, ItemStack itemstack) {
		solidifyingList.put(Integer.valueOf(i), itemstack);
		fillTime.put(Integer.valueOf(i), Integer.valueOf(time));
	}

	public ItemStack getSolidifyingResult(int i) {
		return (ItemStack) solidifyingList.get(Integer.valueOf(i));
	}

	public Map getSolidifyingList() {
		return solidifyingList;
	}
	
	public int getFillTimeFor(int itemid) {
		if(fillTime.get(itemid) != null) {
			return (Integer) fillTime.get(itemid);
		}
		else return 200;
	}
}
