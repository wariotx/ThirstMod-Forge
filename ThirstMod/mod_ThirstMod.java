package net.minecraft.src;

import java.lang.reflect.*;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.*;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.thirstmod.blocks.*;
import net.minecraft.src.thirstmod.containers.*;
import net.minecraft.src.thirstmod.itemmod.*;

public class mod_ThirstMod extends BaseMod {
	private boolean gui = false;
	
	public static final Block waterCollector;
	public static final Block juiceMaker;
	
	public static int JMTop = 3;
	public static int JMFront = 1;
	public static int JMSides = 3;
	public static int WCTop = 0;
	public static int WCSides = 3;
	
	public mod_ThirstMod() {
		ModLoader.registerBlock(waterCollector);
		ModLoader.registerBlock(juiceMaker);
		ModLoader.registerTileEntity(TileEntityJM.class, "Juice Maker");
		ModLoader.registerTileEntity(TileEntityWaterCollector.class, "Water Collector");
		ModLoader.addName(waterCollector, "Rain Collector");
		ModLoader.addName(juiceMaker, "Drinks Brewer");
		
		ModLoader.addRecipe(new ItemStack(juiceMaker, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.glassBottle, });

		ModLoader.addRecipe(new ItemStack(waterCollector, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.bucketEmpty, });

	}

	public String getVersion() {
		return "0.8";
	}

	@Override
	public void load() {
		ModLoader.setInGameHook(this, true, true);
		MinecraftForge.registerSaveHandler(new SaveHandlerMod());
		MinecraftForgeClient.preloadTexture("/thirstmod/textures/icons.png");
		new ConfigHelper();
		new DrinkLoader().loadDrinks();
		
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
			ItemPotion potionObj = (ItemPotion)(new ItemPotionMod(117)).setIconCoord(13, 8).setItemName("potion");
			potion.set(ItemPotion.potion, potionObj);
			
			Field bucket = item.getField("bucketMilk");
			Item bucketMilk = (new ItemBucketMilkMod(79)).setIconCoord(13, 4).setItemName("milk").setContainerItem(Item.bucketEmpty);
			bucket.set(Item.bucketMilk, bucketMilk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onTickInGame(float time, Minecraft minecraft) {
		if(minecraft.thePlayer.capabilities.isCreativeMode == false) {
			Utilities.getStats().onUpdate(minecraft.thePlayer);
			if(gui == false) {
				minecraft.ingameGUI = new net.minecraft.src.thirstmod.GuiIngame(minecraft);
				gui = true;
			}
		}
		return true;
	}
	
	static {
		waterCollector = new BlockWaterCollector(ConfigHelper.rcId, false).setBlockName("waterCollector").setResistance(5F).setHardness(4F);
		juiceMaker = new BlockJM(ConfigHelper.jmId, false).setHardness(5F).setResistance(5F).setStepSound(Block.soundMetalFootstep).setBlockName("juiceMaker");
	}
	
	public static boolean isPlayerJumping() {
		EntityPlayer player = ModLoader.getMinecraftInstance().thePlayer;
		return player.isJumping;
	}
	
	public static String getSaveName() {
		if(ModLoader.getMinecraftInstance().theWorld.saveHandler.getSaveDirectoryName() != null) {
			return ModLoader.getMinecraftInstance().theWorld.saveHandler.getSaveDirectoryName();
		} else {
			return null;
		}
	}
}