package net.minecraft.src.thirstmod;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.thirstmod.blocks.*;
import net.minecraft.src.thirstmod.gui.*;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.world.WorldEvent;

@Mod(modid = "ThirstMod", name = "Thirst Mod", version = "1.0.0")
@NetworkMod(serverSideRequired = false, clientSideRequired = true)
public class ThirstMod implements IGuiHandler {
	public static final Block waterCollector = new BlockRC(232).setBlockName("waterCollector").setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabBlock);
	public static final Block juiceMaker = new BlockJM(233).setBlockName("jm").setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabBlock);
	public static int jmFront = 1;
	public static int rcTop = 0;
	
	@Instance
	public static ThirstMod INSTANCE;
	private DrinkController dc = new DrinkController();
	
	private boolean changedGui = false;
	private boolean isUpdate = false;
	public boolean loadedMod = false;
	
	/**
	 * Called when the mod is loaded.
	 * @param event
	 */
	@Init
	public void onLoad(FMLInitializationEvent event) {
		GameRegistry.registerBlock(waterCollector);
		GameRegistry.registerBlock(juiceMaker);
		GameRegistry.registerTileEntity(TileEntityRC.class, "Rain Collector");
		GameRegistry.registerTileEntity(TileEntityJM.class, "Juice Maker");
		LanguageRegistry.addName(waterCollector, "Rain Collector");
		LanguageRegistry.addName(juiceMaker, "Drinks Brewer");
		GameRegistry.addRecipe(new ItemStack(waterCollector, 1), new Object[] 
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.bucketEmpty, });
		GameRegistry.addRecipe(new ItemStack(juiceMaker, 1), new Object[] 
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.glassBottle, });

		new ContentLoader();
		new DrinkLoader().loadDrinks();
		MinecraftForgeClient.preloadTexture("/thirstmod/textures/icons.png");

		try {
			isUpdate = ThirstUtils.checkForUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		NetworkRegistry.instance().registerGuiHandler(this, this);
		DrinkController.addDrink(Item.potion, 5, 1f);
		DrinkController.addDrink(Item.bucketMilk, 10, 3f);
		DrinkController.addDrink(Item.bowlSoup, 7, 2f);
	}

	/**
	 * Called when 1 game loop is done.
	 * @param minecraft
	 */
	public void onTickInGame(Minecraft minecraft) {
		if(ThirstUtils.getPlayer().capabilities.isCreativeMode == false) {
			if(changedGui == false) {
				minecraft.ingameGUI = new GuiThirst();
				changedGui = true;
			} 
			if(loadedMod == false) {
				if(isUpdate == true) {
					FMLClientHandler.instance().displayGuiScreen(minecraft.thePlayer, new GuiUpdate());
					isUpdate = false;
				}
				ThirstUtils.readNbt(ThirstUtils.getPlayerMp().getEntityData());
				new ThirstUtils();
				loadedMod = true;
			}
			ThirstUtils.getStats().onTick(ThirstUtils.getPlayer());
		}
		dc.onTick(minecraft);
	} 
	
	/**
	 * Called when the Minecraft.currentGui is not null.
	 * @param gui
	 */
	public void onTickInGui(GuiScreen gui) {
		if(gui instanceof GuiMainMenu) {
			ThirstUtils.setDefaults();
		}
	}
	
	/**
	 * Called when the world saves stuff.
	 * @param save
	 */
	@ForgeSubscribe
	public void onSave(WorldEvent.Save save) {
		ThirstUtils.writeNbt(ThirstUtils.getPlayerMp().getEntityData());
	}
	
	/** 
	 * Determines if the player is jumping.
	 * @return
	 */
	public static boolean isJumping() {
		return ThirstUtils.getPlayer().onGround == false;
	}
	
	/**
	 * Sets an icon index for an item.
	 * @param item The Item
	 * @param i number to set it to.
	 */
	public static void setIcon(Item item, int i) {
		item.setIconIndex(i);
	}
	
	/**
	 * Gets the server container. 
	 */
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID) {
			case 90: return new ContainerJM(player.inventory, (TileEntityJM) tile);
			case 91: return new ContainerRC(player.inventory, (TileEntityRC) tile);
		}
		return null;
	}

	/**
	 * Gets the client container.
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch(ID) {
			case 90: return new GuiJM(player.inventory, (TileEntityJM) tile);
			case 91: return new GuiRC(player.inventory, (TileEntityRC) tile);
		}
		return null;
	}
}