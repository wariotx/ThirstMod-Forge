package net.minecraft.src.thirstmod;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.*;

public class DrinkLoader {
	public static Item freshWater;
	public static Item milk;

	public void loadDrinks() {
		freshWater = ((Drink) new Drink(ConfigHelper.freshWaterId, 6, 2.3f, false).setItemName("freshWater").setIconCoord(2, 1)).setTexFile("/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize);
		DrinkController.addOtherDrink(freshWater);
		LanguageRegistry.addName(freshWater, "Fresh Water");
		GameRegistry.addSmelting(Item.potion.shiftedIndex, new ItemStack(freshWater, 1), 0.3f);
		GameRegistry.addShapelessRecipe(new ItemStack(freshWater, 1), new Object[]
		{ ThirstMod.Filter, new ItemStack(Item.potion, 0) });

		if (ConfigHelper.wantMilk == true) {
			DrinkController.addOtherDrink(milk);
			milk = ((Drink) new Drink(ConfigHelper.milkId, 6, 1.2f, false).setIconCoord(0, 1).setItemName("milkBottle")).setTexFile("/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize);
			GameRegistry.addRecipe(new ItemStack(milk, 3), new Object[]
			{ " * ", "^^^", Character.valueOf('*'), Item.bucketMilk, Character.valueOf('^'), Item.glassBottle });
			LanguageRegistry.addName(milk, "Milk Bottle");
		}
		if (ConfigHelper.wantCMilk == true && ConfigHelper.wantMilk == true) {
			Item cMilk = ((Drink) new Drink(ConfigHelper.cMilkId, 5, 1.1f, false).setIconCoord(1, 1).setItemName("chocolateMilk")).setTexFile("/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize);
			GameRegistry.addShapelessRecipe(new ItemStack(cMilk, 1), new Object[]
			{ milk, new ItemStack(Item.dyePowder, 1, 3), Item.sugar, });
			LanguageRegistry.addName(cMilk, "Chocolate Milk");
			DrinkController.addOtherDrink(cMilk);
		}
		if (ConfigHelper.wantFBucket == true) {
			Item fBucket = ((Drink) ((Drink) new Drink(ConfigHelper.fBucketId, 10, 1.4f, false).setIconCoord(4, 1).setItemName("freshBucket").setMaxStackSize(ConfigHelper.maxStackSize)).setReturn(Item.bucketEmpty)).setTexFile("/thirstmod/textures/icons.png");
			LanguageRegistry.addName(fBucket, "Fresh Water Bucket");
			GameRegistry.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(fBucket, 1), 0.4f);
			ModLoader.addShapelessRecipe(new ItemStack(fBucket, 1), new Object[]
			{ Item.bucketWater, ThirstMod.Filter, });
			DrinkController.addOtherDrink(fBucket);
		}
	}
}
