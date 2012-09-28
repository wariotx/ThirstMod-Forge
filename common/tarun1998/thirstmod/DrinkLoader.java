package tarun1998.thirstmod;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.*;

public class DrinkLoader {
	public static Item freshWater;
	public static Item milk;
	public static Item woodGlass = new ItemThirst(ConfigHelper.woodGlassId).setItemName("woodGlass").setIconCoord(5, 1).setCreativeTab(CreativeTabs.tabFood);
	public static Item woodWater = ((Drink) ((Drink) ((Drink) new Drink(ConfigHelper.woodWaterId, 3, 1.2f, false).setItemName("woodWater")).setTexFile("/tarun1998/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize)).setReturn(woodGlass)).setPoisoningChance(0.3f).setIconCoord(6, 1);
	public static Item woodFWater = ((Drink) ((Drink) new Drink(ConfigHelper.woodFWaterId, 5, 1.6f, false).setItemName("woodFWater")).setTexFile("/tarun1998/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize)).setReturn(woodGlass).setIconCoord(7, 1);
	public static Item fBucket;
	
	public void loadDrinks() {
		LanguageRegistry.addName(woodGlass, "Empty Glass");
		LanguageRegistry.addName(woodWater, "Glass of Water");
		LanguageRegistry.addName(woodFWater, "Glass of Clean Water");
		
		GameRegistry.addRecipe(new ItemStack(woodGlass), new Object[]
		{ "* *", "* *", " * ", Character.valueOf('*'), Block.planks, });
		GameRegistry.addShapelessRecipe(new ItemStack(woodFWater), new Object[]
		{ ThirstMod.Filter, woodWater, });
		
		freshWater = ((Drink) new Drink(ConfigHelper.freshWaterId, 7, 2f, false).setItemName("freshWater").setIconCoord(2, 1)).setTexFile("/tarun1998/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize);
		LanguageRegistry.addName(freshWater, "Fresh Water");
		GameRegistry.addSmelting(Item.potion.shiftedIndex, new ItemStack(freshWater, 1), 0.3f);
		GameRegistry.addShapelessRecipe(new ItemStack(freshWater, 1), new Object[]
		{ ThirstMod.Filter, new ItemStack(Item.potion, 0) });

		if (ConfigHelper.wantMilk == true) {
			milk = ((Drink) new Drink(ConfigHelper.milkId, 6, 1.8f, false).setIconCoord(0, 1).setItemName("milkBottle")).setTexFile("/tarun1998/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize);
			GameRegistry.addRecipe(new ItemStack(milk, 3), new Object[]
			{ " * ", "^^^", Character.valueOf('*'), Item.bucketMilk, Character.valueOf('^'), Item.glassBottle });
			LanguageRegistry.addName(milk, "Milk Bottle");
		}
		if (ConfigHelper.wantCMilk == true && ConfigHelper.wantMilk == true) {
			Item cMilk = ((Drink) new Drink(ConfigHelper.cMilkId, 5, 1.6f, false).setIconCoord(1, 1).setItemName("chocolateMilk")).setTexFile("/tarun1998/thirstmod/textures/icons.png").setMaxStackSize(ConfigHelper.maxStackSize);
			GameRegistry.addShapelessRecipe(new ItemStack(cMilk, 1), new Object[]
			{ milk, new ItemStack(Item.dyePowder, 1, 3), Item.sugar, });
			LanguageRegistry.addName(cMilk, "Chocolate Milk");
		}
		if (ConfigHelper.wantFBucket == true) {
			fBucket = ((Drink) ((Drink) new Drink(ConfigHelper.fBucketId, 10, 4f, false).setIconCoord(4, 1).setItemName("freshBucket").setMaxStackSize(ConfigHelper.maxStackSize)).setReturn(Item.bucketEmpty)).setTexFile("/tarun1998/thirstmod/textures/icons.png");
			LanguageRegistry.addName(fBucket, "Fresh Water Bucket");
			GameRegistry.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(fBucket, 1), 0.4f);
			/* This doesn't work. Need to find an alternative way. Perhaps a new TileEntity.
			 * ModLoader.addShapelessRecipe(new ItemStack(fBucket, 1), new
			 * Object[] { Item.bucketWater, ThirstMod.Filter, });
			 */
		}
	}
}
