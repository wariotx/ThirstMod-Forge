package net.minecraft.src.thirstmod;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

public class DrinkLoader {
	public static ItemDrink freshWater;
	
	public static ItemDrink milk;
	
	public void loadDrinks() {
		freshWater = (ItemDrink) new ItemDrink(ConfigHelper.freshWaterId, 6, 2.3f, false).setItemName("freshWater").setIconCoord(2, 1);
		ModLoader.addName(freshWater, "Fresh Water");
		ModLoader.addSmelting(Item.potion.shiftedIndex, new ItemStack(freshWater, 1));
		
		if(ConfigHelper.wantMilk == true) {
			milk = (ItemDrink) new ItemDrink(ConfigHelper.milkId, 6, 1.2f, false).setIconCoord(0, 1).setItemName("milkBottle");
			ModLoader.addRecipe(new ItemStack(milk, 1), new Object[] {
				" * ", "^^^", Character.valueOf('*'), Item.bucketMilk, Character.valueOf('^'), Item.glassBottle
			});
			ModLoader.addName(milk, "Milk Bottle");
		}
		if(ConfigHelper.wantCMilk == true && ConfigHelper.wantMilk == true) {
			ItemDrink cMilk = (ItemDrink) new ItemDrink(ConfigHelper.cMilkId, 5, 1.1f, false).setIconCoord(1, 1).setItemName("chocolateMilk");
			ModLoader.addShapelessRecipe(new ItemStack(cMilk, 1), new Object[] {
				milk, new ItemStack(Item.dyePowder, 1, 3), Item.sugar,
			});
			ModLoader.addName(cMilk, "Chocolate Milk");
		}
		if(ConfigHelper.wantFBucket == true) {
			ItemDrink fBucket = (ItemDrink) new ItemDrink(ConfigHelper.fBucketId, 10, 1.4f, false).setIconCoord(4, 1).setItemName("freshBucket");
			ModLoader.addName(fBucket, "Fresh Water Bucket");
			ModLoader.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(fBucket, 1));
		}
	}
}
