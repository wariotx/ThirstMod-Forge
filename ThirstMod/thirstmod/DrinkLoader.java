package net.minecraft.src.thirstmod;

import java.lang.reflect.Field;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.thirstmod.vanilla.*;

public class DrinkLoader {
	public static Item freshWater;
	public static Item milk;

	public void loadDrinks() {
		freshWater = ((Drink) new Drink(ConfigHelper.freshWaterId, 6, 2.3f, false).setItemName("freshWater").setIconCoord(2, 1)).setTexFile("/thirstmod/textures/icons.png");
		LanguageRegistry.addName(freshWater, "Fresh Water");
		GameRegistry.addSmelting(Item.potion.shiftedIndex, new ItemStack(freshWater, 1), 0.3f);

		if (ConfigHelper.wantMilk == true) {
			milk = ((Drink) new Drink(ConfigHelper.milkId, 6, 1.2f, false).setIconCoord(0, 1).setItemName("milkBottle")).setTexFile("/thirstmod/textures/icons.png");
			GameRegistry.addRecipe(new ItemStack(milk, 3), new Object[]
			{ " * ", "^^^", Character.valueOf('*'), Item.bucketMilk, Character.valueOf('^'), Item.glassBottle });
			LanguageRegistry.addName(milk, "Milk Bottle");
		}
		if (ConfigHelper.wantCMilk == true && ConfigHelper.wantMilk == true) {
			Item cMilk = ((Drink) new Drink(ConfigHelper.cMilkId, 5, 1.1f, false).setIconCoord(1, 1).setItemName("chocolateMilk")).setTexFile("/thirstmod/textures/icons.png");
			GameRegistry.addShapelessRecipe(new ItemStack(cMilk, 1), new Object[]
			{ milk, new ItemStack(Item.dyePowder, 1, 3), Item.sugar, });
			LanguageRegistry.addName(cMilk, "Chocolate Milk");
		}
		if (ConfigHelper.wantFBucket == true) {
			Item fBucket = ((Drink) new Drink(ConfigHelper.fBucketId, 10, 1.4f, false).setIconCoord(4, 1).setItemName("freshBucket")).setTexFile("/thirstmod/textures/icons.png");
			LanguageRegistry.addName(fBucket, "Fresh Water Bucket");
			GameRegistry.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(fBucket, 1), 0.4f);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
