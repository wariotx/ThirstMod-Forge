package net.minecraft.src;

import java.io.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.api.ThirstAPI;
import net.minecraft.src.ThirstMod.api.ThirstStatsAPI;
import net.minecraft.src.ThirstMod.blocks.*;
import net.minecraft.src.ThirstMod.core.*;
import net.minecraft.src.ThirstMod.core.content.ContentLoader;
import net.minecraft.src.ThirstMod.drinks.*;
import net.minecraft.src.ThirstMod.items.*;
import net.minecraft.src.ThirstMod.other.*;
import net.minecraft.src.ThirstMod.guis.*;
import net.minecraft.src.ThirstMod.containers.*;

public class mod_ThirstMod extends BaseModMp
{
	private static Configuration config = new Configuration(new File(Minecraft.getMinecraftDir() + "/mods/ThirstMod/Config.txt"));
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
	public static boolean oldTextures;

	// Texture Overrides
	public static String overrideItem = "/gui/items.png";
	private static String overrideBlock = "/terrain.png";
	public static int JMTop = 62;
	public static int JMFront = ModLoader.addOverride(overrideBlock, "/ThirstMod/textures/juiceMakerFront.png");
	public static int JMSides = 62;
	public static int WCTop = ModLoader.addOverride(overrideBlock, "/ThirstMod/textures/rainCollectorTop.png");
	public static int WCSides = 62;

	// Block Rendering
	public static int dCauldronToptxt;
	public static int dCauldronRenderId;

	// Other Item/Drinks
	public static final Item wheatGrassItem = (new Item(mod_ThirstMod.idWheatGrassItem)).setItemName("wheatGrassItem");
	public static final Item cocoaBean = (new Item(mod_ThirstMod.idCocoaBean)).setItemName("cocoaBean");
	public static final Block cocoa = new BlockCocoa(mod_ThirstMod.idCocoaPlant, ModLoader.addOverride(overrideBlock, "/ThirstMod/textures/cocoa.png")).setBlockName("cocoa").setHardness(0F).setResistance(0.1F);
	public static final Item chocolateDrink = ((Drink) (new Drink(mod_ThirstMod.idChocolateMilk, 5, 2.2F, false)).setItemName("chocalateDrink"));
	public static final Item milkDrink = ((Drink) (new Drink(mod_ThirstMod.idMilkBottle, 6, 4.2F, false)).setItemName("milkDrink"));
	public static final Item boiledWater = ((Drink) (new Drink(mod_ThirstMod.BoiledWaterId, 5, 6.2F, false)).setItemName("Distilled Water"));
	public static final Item freshWater = (new Drink(mod_ThirstMod.idFreshWater, 5, 1.2F, false)).setItemName("freshWater");

	// Wood Bottle Stuff
	public static final Item woodBottle = new ItemWoodBottle(idWoodBottle).setItemName("woodBottle");
	public static final Item waterWood = new ItemWoodDrink(idWaterWood, 4, 1.2f).setPoisoningChance(0.2f).setItemName("waterWood");
	public static final Item dWood = new ItemWoodDrink(idDWood, 5, 1.2f).setItemName("wooddBottle");

	// Other
	public static GuiScreen guiscreen1;
	public static boolean canRainOn;

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
		canteen.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/canteen.png");
		boiledWaterBucket.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/freshBucket.png");
		fCanteen.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/canteen2.png");
		Filter.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/filter.png");
		dFilter.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/dFilter.png");
		rainMaker.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/rainMaker.png");
		sunner.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/rainStopper.png");
		wheatGrassItem.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/wheatGrass.png");
		cocoaBean.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/cocoaBeans.png");
		chocolateDrink.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/milkChocolate.png");
		milkDrink.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/milk.png");
		boiledWater.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/freshWater.png");
		freshWater.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/freshWater2.png");
		woodBottle.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/woodBottle.png");
		waterWood.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/waterWood.png");
		dWood.iconIndex = ModLoader.addOverride(overrideItem, "/ThirstMod/textures/dWaterWood.png");

		// Add Names
		ModLoader.addName(boiledWaterBucket, "Boiled-Water Bucket");
		ModLoader.addName(waterCollector, "Rain Collector");
		ModLoader.addName(juiceMaker, "Drinks Brewer");
		ModLoader.addName(Filter, "Water Filter");
		ModLoader.addName(dFilter, "Dirty Filter");
		ModLoader.addName(distilledCauldron, "Boiled Cauldron");
		ModLoader.addName(rainMaker, "Rain Maker");
		ModLoader.addName(sunner, "Rain Stopper");
		ModLoader.addName(cocoaBean, "Coffea Bean");

		ModLoader.addName(chocolateDrink, "Milk Chocolate");
		ModLoader.addName(milkDrink, "Milk Bottle");
		ModLoader.addName(boiledWater, "Boiled Water");
		ModLoader.addName(freshWater, "Fresh Water");
		ModLoader.addName(wheatGrassItem, "Wheat Grass");
		ModLoader.addName(woodBottle, "Wood Bottle");
		ModLoader.addName(waterWood, "Water Bottle");
		ModLoader.addName(dWood, "Boiled-Water Bottle");

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

		ModLoader.addName(new ItemStack(fCanteen, 1, 0), "Empty Filtered Canteen");
		ModLoader.addName(new ItemStack(fCanteen, 1, 1), "Filtered Canteen 1 Use");
		ModLoader.addName(new ItemStack(fCanteen, 1, 2), "Filtered Canteen 2 Uses");
		ModLoader.addName(new ItemStack(fCanteen, 1, 3), "Filtered Canteen 3 Uses");
		ModLoader.addName(new ItemStack(fCanteen, 1, 4), "Filtered Canteen 4 Uses");
		ModLoader.addName(new ItemStack(fCanteen, 1, 5), "Full Filtered Canteen");

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

		ModLoader.addRecipe(new ItemStack(woodBottle, 1), new Object[]
		{ "* *", "* *", " * ", Character.valueOf('*'), Block.planks });

		ModLoader.addShapelessRecipe(new ItemStack(woodBottle, 1), new Object[]
		{ Block.dirt });

		ModLoader.setInGameHook(this, true, true);
		ModLoader.setInGUIHook(this, true, true);

		dCauldronToptxt = ModLoader.getUniqueSpriteIndex("/terrain.png");
		dCauldronRenderId = ModLoader.getUniqueBlockModelID(this, false);

		ModLoader.addSmelting(Item.bucketWater.shiftedIndex, new ItemStack(boiledWaterBucket, 1));
		ModLoader.addSmelting(waterWood.shiftedIndex, new ItemStack(dWood, 1));
		ModLoader.addSmelting(canteen.shiftedIndex, new ItemStack(canteen, 1, 10));

		ModLoader.registerBlock(cocoa);
		cocoa.blockIndexInTexture = ModLoader.addOverride(overrideBlock, "/ThirstMod/textures/cocoa2.png");// cocoa2
		ModLoader.addName(cocoa, "Coffea");
	}

	public void load()
	{
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

		ContentLoader.loadContent();
	}

	public String getVersion()
	{
		return ThirstUtils.getVersion();
	}

	public boolean onTickInGame(float f, Minecraft minecraft)
	{
		thirstModCore.onTickInGame(f, minecraft);

		if (minecraft.currentScreen == null)
		{
			guiscreen1 = null;
		}

		if (minecraft.theWorld.isRemote)
		{
			if (ThirstUtils.classExists("ModLoaderMp"))
			{
				sendMovement();
			}
		}
		
		ThirstAPI.register(new ThirstStatsAPI(), ModLoader.getMinecraftInstance().thePlayer);
		return true;
	}

	public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen)
	{
		thirstModCore.onTickInGUI(f, minecraft, guiscreen);
		if ((guiscreen instanceof GuiContainerCreative) && !(guiscreen1 instanceof GuiContainerCreative))
		{
			Container container = ((GuiContainer) guiscreen).inventorySlots;
			List list = ((ContainerCreative) container).itemList;
			for (int var1 = 0; var1 < ThirstUtils.getBlock().size(); ++var1)
			{
				list.add(ThirstUtils.getBlock().get(var1));
			}
			for (int i = 0; i < ThirstUtils.getItem().size(); ++i)
			{
				list.add(ThirstUtils.getItem().get(i));
			}
			guiscreen1 = guiscreen;
		}
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
		oldTextures = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Light Blue Textures", Configuration.CATEGORY_GENERAL, false).value);

		config.save();
		return BoiledWaterId;
	}

	public boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l)
	{
		if (l == dCauldronRenderId)
		{
			return RenderDCauldron(block, i, j, k, renderblocks);
		}
		return false;
	}

	public boolean RenderDCauldron(Block block, int i, int j, int k, RenderBlocks renderblocks)
	{
		IBlockAccess blockAccess = renderblocks.blockAccess;
		renderblocks.renderStandardBlock(block, i, j, k);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, i, j, k));
		float f = 1.0F;
		int l = block.colorMultiplier(blockAccess, i, j, k);
		float f1 = (float) (l >> 16 & 0xff) / 255F;
		float f2 = (float) (l >> 8 & 0xff) / 255F;
		float f3 = (float) (l & 0xff) / 255F;
		if (EntityRenderer.anaglyphEnable)
		{
			float f6 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
			float f4 = (f1 * 30F + f2 * 70F) / 100F;
			float f7 = (f1 * 30F + f3 * 70F) / 100F;
			f1 = f6;
			f2 = f4;
			f3 = f7;
		}
		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		char c = '\232';
		float f5 = 0.125F;
		renderblocks.renderSouthFace(block, ((float) i - 1.0F) + f5, j, k, c);
		renderblocks.renderNorthFace(block, ((float) i + 1.0F) - f5, j, k, c);
		renderblocks.renderWestFace(block, i, j, ((float) k - 1.0F) + f5, c);
		renderblocks.renderEastFace(block, i, j, ((float) k + 1.0F) - f5, c);
		char c1 = '\213';
		renderblocks.renderTopFace(block, i, ((float) j - 1.0F) + 0.25F, k, c1);
		renderblocks.renderBottomFace(block, i, ((float) j + 1.0F) - 0.75F, k, c1);
		int i1 = blockAccess.getBlockMetadata(i, j, k);
		if (i1 > 0)
		{
			int c2 = dCauldronToptxt;
			if (i1 > 3)
			{
				i1 = 3;
			}
			renderblocks.renderTopFace(block, i, ((float) j - 1.0F) + (6F + (float) i1 * 3F) / 16F, k, c2);
		}
		return true;
	}

	public void registerAnimation(Minecraft minecraft)
	{
		ModLoader.addAnimation(new TextureDistilledWaterFX());
	}

	public void handlePacket(Packet230ModLoader packet230modloader)
	{
		super.handlePacket(packet230modloader);
		new ServerHandler().handlePacket(packet230modloader);
	}

	public GuiScreen handleGUI(int i)
	{
		if (i == 90)
		{
			return new GuiWaterCollector(ModLoader.getMinecraftInstance().thePlayer.inventory, new TileEntityWaterCollector());
		}
		if (i == 91)
		{
			return new GuiJM(ModLoader.getMinecraftInstance().thePlayer.inventory, new TileEntityJM());
		} else
		{
			return null;
		}
	}

	public void generateSurface(World world, Random random, int i, int j)
	{
		for (int coca1 = 0; coca1 < 2; coca1++)
		{
			int x = i + random.nextInt(16) + 4;
			int y = random.nextInt(128);
			int z = j + random.nextInt(16) + 3;
			(new WorldGenDeadBush(cocoa.blockID)).generate(world, random, x, y, z);
		}
	}

	public void registerPlayerBase()
	{
		if (ThirstUtils.classExists("PlayerAPI"))
		{
			ThirstUtils.print("PlayerAPI Found");
			PlayerAPI.register("ThirstMod", PlayerThirst.class);
		}
	}

	public void registerModLoaderMp()
	{
		if (ThirstUtils.classExists("ModLoaderMp"))
		{
			ModLoaderMp.registerGUI(this, 90);
			ModLoaderMp.registerGUI(this, 91);
		}
	}

	public void sendMovement()
	{
		int dataInt[] = new int[1];
		dataInt[0] = ThirstUtils.getMovementStat(ModLoader.getMinecraftInstance().thePlayer);
		Packet230ModLoader packet = new Packet230ModLoader();
		packet.packetType = 0;
		packet.dataInt = dataInt;
		ModLoaderMp.sendPacket(this, packet);
	}
	
	public static WorldInfo getWorldInfo()
	{
		return ModLoader.getMinecraftInstance().theWorld.worldInfo;
	}

	public static String getSaveName()
	{
		return ModLoader.getMinecraftInstance().theWorld.saveHandler.getSaveDirectoryName();
	}

	public static boolean isJumping()
	{
		return ModLoader.getMinecraftInstance().thePlayer.isJumping;
	}

	public static void setSpeedInAir(float f)
	{
		ModLoader.getMinecraftInstance().thePlayer.speedInAir = f;
	}

	public static void setSpeedOnGround(float f)
	{
		ModLoader.getMinecraftInstance().thePlayer.speedOnGround = f;
	}

	public static void setIconIndex(Item item, int i)
	{
		item.iconIndex = i;
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