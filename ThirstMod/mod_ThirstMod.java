package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.*;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.thirstmod.blocks.*;
import net.minecraft.src.thirstmod.containers.*;
import net.minecraft.src.thirstmod.content.ContentMain;
import net.minecraft.src.thirstmod.itemmod.*;
import net.minecraft.src.thirstmod.items.*;

public class mod_ThirstMod extends BaseMod {
	private boolean gui = false;
	
	public static final Block waterCollector;
	public static final Block juiceMaker;
	
	public static final Item filter;
	public static final Item dFilter;
	
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
		ModLoader.addName(filter, "Clean Filter");
		ModLoader.addName(dFilter, "Dirty Filter");
		
		ModLoader.addRecipe(new ItemStack(juiceMaker, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.glassBottle, });

		ModLoader.addRecipe(new ItemStack(waterCollector, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.bucketEmpty, });
	}

	public String getVersion() {
		return "0.8";
	}

	public void load() {
		ModLoader.setInGameHook(this, true, true);
		MinecraftForge.registerSaveHandler(new SaveHandlerMod());
		MinecraftForgeClient.preloadTexture("/thirstmod/textures/icons.png");
		new ConfigHelper();
		new DrinkLoader().loadDrinks();
		ContentMain.loadContent();
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
		
		dFilter = new ItemFilter(ConfigHelper.dFilterId).setItemName("dfilter").setIconCoord(1, 2);
		filter = new ItemFilter(ConfigHelper.filterId).setItemName("filter").setIconCoord(0, 2).setContainerItem(dFilter);
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
	
	public static void setIconIndex(Item item, int i) {
		item.iconIndex = i;
	}
}