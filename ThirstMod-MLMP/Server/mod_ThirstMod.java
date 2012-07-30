/*
 * WARNING!
 * Anyone adding stuff to this file must go by the following requirements:
 * Add the ID's, Items, Blocks or anything else to their respective categories. Failure to this will mean that Tarun will kill you. Medievor...
 * If it is something that controls exhaustion or anything relating to the bar, put it in ThirstModCore.class
 */

package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.blocks.*;
import net.minecraft.src.ThirstMod.containers.TileEntityWaterCollector;
import net.minecraft.src.ThirstMod.core.*;
import net.minecraft.src.ThirstMod.drinks.*;
import net.minecraft.src.ThirstMod.items.*;
import net.minecraft.src.ThirstMod.other.PlayerThirst;
import net.minecraft.src.ThirstMod.containers.*;

public class mod_ThirstMod extends BaseModMp
{
	private static Configuration config = new Configuration(new File(ThirstUtils.getDir() + "/mods/ThirstMod/Config.txt"));
	public ThirstModCore thirstModCore = new ThirstModCore();

	// Blocks
	public static final Block waterCollector;
	public static final Block distilledCauldron;
	public static final Block juiceMaker;
	public static final Item boiledWaterBucket;

	// Items
	public static final Item Filter;
	public static final Item dFilter;
	public static final Item canteen;
	public static final Item fCanteen;
	public static final Item rainMaker;
	public static final Item sunner;

	// Block/Item ID's
	public static int BoiledWaterId = setupConfig();
	public static int CanteenId;
	public static int boiledWaterBucketId;
	public static int waterCollectorId;
	public static int distilledCauldronId;
	public static int idRainMaker;
	public static int idFilter;
	public static int idUsedFilter;
	public static int idFCanteen;
	public static int idJuiceMaker;
	public static int idChocolateMilk;
	public static int idMilkBottle;
	public static int idDaytimeMaker;
	public static int idFreshWater;
	public static int idWheatGrassItem;
	public static int idCocoaBean;
	public static int idCocoaPlant;
	public static int idWoodBottle;
	public static int idWaterWood;
	public static int idDWood;

	// Configuration
	public static boolean thirstOn;
	public static int thirstRate;
	public static boolean isThirstPoisoningOn;
	public static boolean thirstInPeaceful;
	public static boolean isThirstMeterOnLeft;
	public static int maxStackSize;

	// Texture Overrides
	public static String overrideItem = "/gui/items.png";
	private static String overrideBlock = "/terrain.png";
	public static int JMTop = 62;
	public static int JMFront = ModLoader.addOverride(overrideBlock, "/ThirstMod/Blocks/juiceMakerFront.png");
	public static int JMSides = 62;
	public static int WCTop = ModLoader.addOverride(overrideBlock, "/ThirstMod/Blocks/rainCollectorTop.png");
	public static int WCSides = 62;

	// Block Rendering
	public static int dCauldronToptxt;
	public static int dCauldronRenderId;

	// Other Item/Drinks
	public static final Item wheatGrassItem = (new Item(mod_ThirstMod.idWheatGrassItem)).setItemName("wheatGrassItem");
	public static final Item cocoaBean = (new Item(mod_ThirstMod.idCocoaBean)).setItemName("cocoaBean");
	public static final Block cocoa = new BlockCocoa(mod_ThirstMod.idCocoaPlant, ModLoader.addOverride(overrideBlock, "/ThirstMod/Blocks/cocoa.png")).setBlockName("cocoa").setHardness(0F).setResistance(0.1F);
	public static final Item chocolateDrink = ((Drink) (new Drink(mod_ThirstMod.idChocolateMilk, 5, 2.2F, false)).setItemName("chocalateDrink"));
	public static final Item milkDrink = ((Drink) (new Drink(mod_ThirstMod.idMilkBottle, 6, 4.2F, false)).setItemName("milkDrink"));
	public static final Item boiledWater = ((Drink) (new Drink(mod_ThirstMod.BoiledWaterId, 5, 6.2F, false)).setItemName("Distilled Water"));
	public static final Item freshWater = (new Drink(mod_ThirstMod.idFreshWater, 5, 1.2F, false)).setItemName("freshWater");

	// Wood Bottle Stuff
	public static final Item woodBottle = new ItemWoodBottle(idWoodBottle).setItemName("woodBottle");
	public static final Item waterWood = new ItemWoodDrink(idWaterWood, 4, 1.2f)/*.setPoisoningChance(0.2f)*/.setItemName("waterWood");
	public static final Item dWood = new ItemWoodDrink(idDWood, 5, 1.2f).setItemName("wooddBottle");
	
	//
	public static mod_ThirstMod mod;

	public mod_ThirstMod()
	{
		// Register Blocks
		ModLoader.registerBlock(waterCollector);
		ModLoader.registerBlock(distilledCauldron);
		ModLoader.registerBlock(juiceMaker);

		// Register Tile Entities
		ModLoader.registerTileEntity(TileEntityJM.class, "Juice Maker");
		ModLoader.registerTileEntity(TileEntityWaterCollector.class, "Water Collector");

		// Block/Item Overrides
		canteen.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/canteen.png");
		boiledWaterBucket.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/freshBucket.png");
		fCanteen.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/canteen2.png");
		Filter.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/filter.png");
		dFilter.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/dFilter.png");
		rainMaker.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/rainMaker.png");
		sunner.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/rainStopper.png");
		wheatGrassItem.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/wheatGrass.png");
		cocoaBean.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/cocoaBeans.png");
		chocolateDrink.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/milkChocolate.png");
		milkDrink.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/milk.png");
		boiledWater.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/freshWater.png");
		freshWater.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/freshWater2.png");
		woodBottle.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/woodBottle.png");
		waterWood.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/waterWood.png");
		dWood.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/Items/dWaterWood.png");

		// Recipes
		ModLoader.addRecipe(new ItemStack(canteen, 1, 0), new Object[]
		{ "L L", " L ", Character.valueOf('L'), Item.leather });

		ModLoader.addRecipe(new ItemStack(Filter, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.planks, Character.valueOf('#'), Item.silk, });

		ModLoader.addShapelessRecipe(new ItemStack(Filter, 1), new Object[]
		{ dFilter, Item.silk });

		ModLoader.addShapelessRecipe(new ItemStack(fCanteen, 1, 5), new Object[]
		{ Filter, new ItemStack(canteen, 1, 5) });

		ModLoader.addRecipe(new ItemStack(juiceMaker, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.glassBottle, });

		ModLoader.addRecipe(new ItemStack(waterCollector, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.bucketEmpty, });

		ModLoader.addRecipe(new ItemStack(milkDrink, 3), new Object[]
		{ " * ", "&&&", Character.valueOf('*'), Item.bucketMilk, Character.valueOf('&'), Item.glassBottle, });

		ModLoader.addShapelessRecipe(new ItemStack(chocolateDrink, 1), new Object[]
		{ new ItemStack(Item.dyePowder, 1, 3), milkDrink, });

		ModLoader.addSmelting(Item.potion.shiftedIndex, new ItemStack(boiledWater, 1));

		ModLoader.addRecipe(new ItemStack(mod_ThirstMod.boiledWaterBucket, 1), new Object[]
		{ "***", " # ", Character.valueOf('*'), boiledWater, Character.valueOf('#'), Item.bucketEmpty, });

		ModLoader.addShapelessRecipe(new ItemStack(freshWater, 1), new Object[]
		{ mod_ThirstMod.Filter, new ItemStack(Item.potion, 0) });

		ModLoader.addShapelessRecipe(new ItemStack(mod_ThirstMod.boiledWaterBucket, 1), new Object[]
		{ Item.bucketWater, mod_ThirstMod.Filter, });

		ModLoader.addShapelessRecipe(new ItemStack(wheatGrassItem, 1), new Object[]
		{ new ItemStack(Block.tallGrass, 1, 1), Item.wheat, });

		ModLoader.setInGameHook(this, true, true);

		dCauldronToptxt = ModLoader.getUniqueSpriteIndex("/terrain.png");
		dCauldronRenderId = ModLoader.getUniqueBlockModelID(this, false);

		ModLoader.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(boiledWaterBucket, 1));
		ModLoader.addSmelting(waterWood.shiftedIndex, new ItemStack(dWood, 1));
		ModLoader.addSmelting(canteen.shiftedIndex, new ItemStack(canteen, 1, 10));
		
		ModLoader.registerBlock(cocoa);
		cocoa.blockIndexInTexture = ModLoader.addOverride(overrideBlock, "/ThirstMod/Blocks/cocoa2.png");// cocoa2
		
		mod = this;
	}

	public void load()
	{
		ContentLoader.loadContent();
		registerPlayerBase();
		registerModLoaderMp();

		ThirstUtils.addRCRecipe(Item.glassBottle.shiftedIndex, new ItemStack(freshWater, 1));
		ThirstUtils.addRCRecipe(Item.bucketEmpty.shiftedIndex, new ItemStack(boiledWaterBucket, 1));
		ThirstUtils.addRCRecipe(canteen.shiftedIndex, new ItemStack(canteen, 1, 10));
		ThirstUtils.addBlock(juiceMaker);
		ThirstUtils.addBlock(waterCollector);
		ThirstUtils.addSubtypeItem(canteen, 5);
		ThirstUtils.addSubtypeItem(fCanteen, 5);
		ThirstUtils.addSubtypeItem(canteen, 10);
		ThirstUtils.addBlock(cocoa);
	}

	public String getVersion()
	{
		return ThirstUtils.getVersion();
	}

	public boolean onTickInGame(float f, MinecraftServer minecraft)
	{
		return true;
	}
	
	public static int setupConfig()
	{
		config.load();

		BoiledWaterId = Integer.parseInt(config.getOrCreateIntProperty("Distilled Water Bottle Id", Configuration.CATEGORY_ITEM, 401).value);
		CanteenId = Integer.parseInt(config.getOrCreateIntProperty("Canteen Id", Configuration.CATEGORY_ITEM, 400).value);
		boiledWaterBucketId = Integer.parseInt(config.getOrCreateIntProperty("Distilled Water Bucket Id", Configuration.CATEGORY_ITEM, 402).value);
		waterCollectorId = Integer.parseInt(config.getOrCreateBlockIdProperty("Water Collector Id", 195).value);
		distilledCauldronId = Integer.parseInt(config.getOrCreateBlockIdProperty("Distilled Cauldron Id", 196).value);
		idJuiceMaker = Integer.parseInt(config.getOrCreateBlockIdProperty("Juice Maker Id", 197).value);
		idCocoaPlant = Integer.parseInt(config.getOrCreateBlockIdProperty("Cocoa Plant", 198).value);

		idFilter = Integer.parseInt(config.getOrCreateIntProperty("Clean Filter Id", Configuration.CATEGORY_ITEM, 403).value);
		idUsedFilter = Integer.parseInt(config.getOrCreateIntProperty("Dirty Filter Id", Configuration.CATEGORY_ITEM, 404).value);
		idFCanteen = Integer.parseInt(config.getOrCreateIntProperty("Filtered Canteen Id", Configuration.CATEGORY_ITEM, 405).value);
		idChocolateMilk = Integer.parseInt(config.getOrCreateIntProperty("Chocolate Milk Id", Configuration.CATEGORY_ITEM, 408).value);
		idMilkBottle = Integer.parseInt(config.getOrCreateIntProperty("Milk Bottle Id", Configuration.CATEGORY_ITEM, 409).value);
		idRainMaker = Integer.parseInt(config.getOrCreateIntProperty("Rain Maker", Configuration.CATEGORY_ITEM, 413).value);
		idDaytimeMaker = Integer.parseInt(config.getOrCreateIntProperty("Rain Stopper", Configuration.CATEGORY_ITEM, 414).value);
		idFreshWater = Integer.parseInt(config.getOrCreateIntProperty("Fresh Water", Configuration.CATEGORY_ITEM, 417).value);
		idWheatGrassItem = Integer.parseInt(config.getOrCreateIntProperty("Wheat Grass Item", Configuration.CATEGORY_ITEM, 419).value);
		idCocoaBean = Integer.parseInt(config.getOrCreateIntProperty("Cocoa Bean", Configuration.CATEGORY_ITEM, 421).value);
		idWoodBottle = Integer.parseInt(config.getOrCreateIntProperty("Wood Bottle", Configuration.CATEGORY_ITEM, 422).value);
		idWaterWood = Integer.parseInt(config.getOrCreateIntProperty("Water Bottle", Configuration.CATEGORY_ITEM, 423).value);
		idDWood = Integer.parseInt(config.getOrCreateIntProperty("Boiled Water Bottle", Configuration.CATEGORY_ITEM, 424).value);

		thirstOn = Boolean.parseBoolean(config.getOrCreateBooleanProperty("ThirstMod On", Configuration.CATEGORY_GENERAL, true).value);
		thirstRate = Integer.parseInt(config.getOrCreateIntProperty("Dehydration Rate", Configuration.CATEGORY_GENERAL, 10).value);
		isThirstPoisoningOn = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Thirst Poisoning", Configuration.CATEGORY_GENERAL, true).value);
		thirstInPeaceful = Boolean.parseBoolean(config.getOrCreateBooleanProperty("ThirstMod in Peaceful", Configuration.CATEGORY_GENERAL, false).value);
		isThirstMeterOnLeft = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Thirst Meter on Left", Configuration.CATEGORY_GENERAL, false).value);
		maxStackSize = Integer.parseInt(config.getOrCreateIntProperty("Max Stack Size", Configuration.CATEGORY_GENERAL, 4).value);

		config.save();
		return BoiledWaterId;
	}

	public void handlePacket(Packet230ModLoader packet230modloader, EntityPlayerMP epm)
	{
		super.handlePacket(packet230modloader, epm);
		if (ThirstUtils.classExists("ModLoaderMp"))
		{
			switch (packet230modloader.packetType)
			{
				case 0:
				{
					PlayerThirst.getStats().instMove = packet230modloader.dataInt[0];
				}
			}
		}
	}

	public void generateSurface(World world, Random random, int i, int j)
	{
		for (int coca1 = 0; coca1 < 2; coca1++)
		{
			int x = i + random.nextInt(3) + 4;
			int y = random.nextInt(128);
			int z = j + random.nextInt(1) + 3;
			(new WorldGenDeadBush(cocoa.blockID)).generate(world, random, x, y, z);
		}
	}

	public void registerPlayerBase()
	{
		//Doesn't seem to work in actual game...
		if (ThirstUtils.classExists("PlayerAPI"))
		{
			ThirstUtils.print("PlayerAPI Found: Using custom exhaustion!");
			PlayerAPI.register("ThirstMod", PlayerThirst.class);
		} else
		{
			ThirstUtils.print("PlayerAPI not found: Not using custom exhaustion!");
		}
	}
	
	public void registerModLoaderMp()
	{
	}
	
	public static WorldInfo getWorldInfo(EntityPlayer epm)
	{
		return epm.worldObj.worldInfo;
	}
	
	static
	{
		waterCollector = new BlockWaterCollector(waterCollectorId, false).setBlockName("waterCollector").setResistance(5F).setHardness(4F);
		juiceMaker = new BlockJM(idJuiceMaker, false).setHardness(5F).setResistance(5F).setStepSound(Block.soundMetalFootstep).setBlockName("juiceMaker");
		distilledCauldron = new BlockDistilledCauldron(distilledCauldronId).setBlockName("Distilled Cauldron").setHardness(2.0F).setRequiresSelfNotify().setResistance(2.5F).setStepSound(Block.soundMetalFootstep);
		boiledWaterBucket = new ItemBoiledWaterBucket(boiledWaterBucketId).setItemName("Distilled-Water Bucket");
		dFilter = (new ItemFilter(idUsedFilter)).setItemName("dFilter");
		Filter = (new ItemFilter(idFilter)).setItemName("Filter").setContainerItem(dFilter);
		canteen = new ItemCanteen(CanteenId).setItemName("Canteen");
		fCanteen = new ItemFCanteen(idFCanteen).setItemName("FilteredCanteen");
		rainMaker = (new ItemRainMaker(idRainMaker)).setItemName("rainMaker");
		sunner = (new ItemSunner(idDaytimeMaker)).setItemName("daytimeMaker");
	}
}