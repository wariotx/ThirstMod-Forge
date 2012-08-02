/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.core;

import java.lang.reflect.Field;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.itemmod.*;
import net.minecraft.src.thirstmod.items.*;

public class DrinkLoader {
	public static ItemDrink freshWater;
	public static ItemDrink milk;
	public static Item canteen;

	public void loadDrinks() {
		freshWater = (ItemDrink) new ItemDrink(ConfigHelper.freshWaterId, 6, 2.3f, false).setItemName("freshWater").setIconCoord(2, 1);
		ModLoader.addName(freshWater, "Fresh Water");
		ModLoader.addSmelting(Item.potion.shiftedIndex, new ItemStack(freshWater, 1));

		canteen = new ItemCanteen(ConfigHelper.canteenId).setItemName("Canteen").setIconCoord(3, 1);
		ModLoader.addName(new ItemStack(canteen, 1, 0), "Empty Canteen");
		ModLoader.addName(new ItemStack(canteen, 1, 1), "Canteen 1 Use");
		ModLoader.addName(new ItemStack(canteen, 1, 2), "Canteen 2 Uses");
		ModLoader.addName(new ItemStack(canteen, 1, 3), "Canteen 3 Uses");
		ModLoader.addName(new ItemStack(canteen, 1, 4), "Canteen 4 Uses");
		ModLoader.addName(new ItemStack(canteen, 1, 5), "Full Canteen");
		ModLoader.addName(new ItemStack(canteen, 1, 6), "Clean Canteen 1 Use");
		ModLoader.addName(new ItemStack(canteen, 1, 7), "Clean Canteen 2 Uses");
		ModLoader.addName(new ItemStack(canteen, 1, 8), "Clean Canteen 3 Uses");
		ModLoader.addName(new ItemStack(canteen, 1, 9), "Clean Canteen 4 Uses");
		ModLoader.addName(new ItemStack(canteen, 1, 10), "Full Clean Canteen");
		ModLoader.addRecipe(new ItemStack(canteen, 1, 0), new Object[]
		{ "L L", " L ", Character.valueOf('L'), Item.leather });

		if (ConfigHelper.wantMilk == true) {
			milk = (ItemDrink) new ItemDrink(ConfigHelper.milkId, 6, 1.2f, false).setIconCoord(0, 1).setItemName("milkBottle");
			ModLoader.addRecipe(new ItemStack(milk, 3), new Object[]
			{ " * ", "^^^", Character.valueOf('*'), Item.bucketMilk, Character.valueOf('^'), Item.glassBottle });
			ModLoader.addName(milk, "Milk Bottle");
		}
		if (ConfigHelper.wantCMilk == true && ConfigHelper.wantMilk == true) {
			ItemDrink cMilk = (ItemDrink) new ItemDrink(ConfigHelper.cMilkId, 5, 1.1f, false).setIconCoord(1, 1).setItemName("chocolateMilk");
			ModLoader.addShapelessRecipe(new ItemStack(cMilk, 1), new Object[]
			{ milk, new ItemStack(Item.dyePowder, 1, 3), Item.sugar, });
			ModLoader.addName(cMilk, "Chocolate Milk");
		}
		if (ConfigHelper.wantFBucket == true) {
			ItemDrink fBucket = (ItemDrink) new ItemDrink(ConfigHelper.fBucketId, 10, 1.4f, false).setIconCoord(4, 1).setItemName("freshBucket");
			ModLoader.addName(fBucket, "Fresh Water Bucket");
			ModLoader.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(fBucket, 1));
		}

		try {
			Item.itemsList[282] = null;
			Item.itemsList[373] = null;
			Item.itemsList[335] = null;

			Class item = Item.class;
			Field soup = item.getField("bowlSoup");
			Item soupObj = (new ItemSoupMod(26, 8)).setIconCoord(8, 4).setItemName("mushroomStew");
			soup.set(Item.bowlSoup, soupObj);

			Class itemPotion = ItemPotion.class;
			Field potion = itemPotion.getField("potion");
			ItemPotion potionObj = (ItemPotion) (new ItemPotionMod(117)).setIconCoord(13, 8).setItemName("potion");
			potion.set(ItemPotion.potion, potionObj);

			Field bucket = item.getField("bucketMilk");
			Item bucketMilk = (new ItemBucketMilkMod(79)).setIconCoord(13, 4).setItemName("milk").setContainerItem(Item.bucketEmpty);
			bucket.set(Item.bucketMilk, bucketMilk);

			// Must make sure this code is called if wantFBucket == true. Too
			// lazy to do everything needed to make the cauldron work.
			// medievor can do it sometime if he pops online.
			/*
			 * Class block = Block.class; Field cauldron =
			 * block.getField("cauldron"); Block cauldronObj;
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		ModLoader.addShapelessRecipe(new ItemStack(DrinkLoader.freshWater), new Object[]
		{ mod_ThirstMod.filter, new ItemStack(Item.potion, 1) });

		ModLoader.addShapelessRecipe(new ItemStack(DrinkLoader.canteen, 1, 10), new Object[]
		{ mod_ThirstMod.filter, new ItemStack(DrinkLoader.canteen, 0, 5) });
	}
}
