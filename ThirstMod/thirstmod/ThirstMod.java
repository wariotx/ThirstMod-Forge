package net.minecraft.src.thirstmod;

import java.util.Random;
import cpw.mods.fml.client.*;
import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.thirstmod.api.*;
import net.minecraft.src.thirstmod.blocks.*;
import net.minecraft.src.thirstmod.gui.*;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.*;

@Mod(modid = ThirstUtils.ID, name = ThirstUtils.NAME, version = ThirstUtils.VERSION)
@NetworkMod(serverSideRequired = false, clientSideRequired = true)
public class ThirstMod implements IGuiHandler, IDrinkAPI {
	public static final Block waterCollector = new BlockRC(ConfigHelper.rcId).setBlockName("waterCollector").setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabBlock);
	public static final Block juiceMaker = new BlockJM(ConfigHelper.jmId).setBlockName("juiceMaker").setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabBlock);
	public static final Item dFilter = (new ItemThirst(ConfigHelper.dFilterId).setItemName("dFilter").setMaxStackSize(1)).setIconIndex(33).setTabToDisplayOn(CreativeTabs.tabMisc);
	public static final Item Filter = (new ItemThirst(ConfigHelper.filterId).setItemName("filter").setMaxStackSize(1)).setContainerItem(dFilter).setIconIndex(32).setTabToDisplayOn(CreativeTabs.tabMisc);

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
		LanguageRegistry.addName(Filter, "Clean Filter");
		LanguageRegistry.addName(dFilter, "Dirty Filter");
		
		GameRegistry.addRecipe(new ItemStack(waterCollector, 1), new Object[] 
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.bucketEmpty, });
		
		GameRegistry.addRecipe(new ItemStack(juiceMaker, 1), new Object[] 
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.glassBottle, });
		
		GameRegistry.addRecipe(new ItemStack(Filter), new Object[] 
		{ "***", "*!*", "***", Character.valueOf('*'), Block.planks, Character.valueOf('!'), Item.silk });
		
		GameRegistry.addShapelessRecipe(new ItemStack(Filter), new Object[]
		{ Item.silk, dFilter });
		
		new ContentLoader();
		new DrinkLoader().loadDrinks();
		new ThirstAPI();
		
		isUpdate = ThirstUtils.checkForUpdate();
		
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		MinecraftForgeClient.preloadTexture("/thirstmod/textures/icons.png");
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		NetworkRegistry.instance().registerGuiHandler(this, this);
		
		APIHooks.registerDrinks();
		DrinkController.addDrink(Item.potion, 5, 1f);
		DrinkController.addDrink(Item.bucketMilk, 10, 3f);
		DrinkController.addDrink(Item.bowlSoup, 7, 2f);
		
		ThirstAPI.registerAPIDrink(this);
	}

	/**
	 * Called when 1 game loop is done.
	 * @param minecraft
	 */
	public void onTickInGame(Minecraft minecraft) {
		if(ThirstUtils.getPlayerMp().capabilities.isCreativeMode == false) {
			if(changedGui == false) {
				minecraft.ingameGUI = new GuiThirst();
				changedGui = true;
			} 
			if(loadedMod == false) {
				if(isUpdate == true) {
					FMLClientHandler.instance().displayGuiScreen(minecraft.thePlayer, new GuiUpdate());
					isUpdate = false;
				}
				onLoadNBT();
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
			ThirstUtils.setModUnloaded();
		}
		if(gui instanceof GuiGameOver) {
			ThirstUtils.setDefaults();
		}
	}
	
	/**
	 * Called when the world saves stuff.
	 * @param save
	 */
	@ForgeSubscribe
	public void onSave(WorldEvent.Save save) {
		try {
			MinecraftServer minecraft = FMLClientHandler.instance().getServer();
			String allNames[] = minecraft.getAllUsernames().clone();
			for(int i = 0; i < allNames.length; i++) {
				EntityPlayerMP player = minecraft.getConfigurationManager().getPlayerForUsername(allNames[i]);
				ThirstUtils.writeNbt(player.getEntityData());
			}
		} catch(Exception e) {
		}
 	}
	
	/**
	 * Loads data from the NBT. Throws exception if something goes wrong.
	 */
	public void onLoadNBT() {
		try {
			MinecraftServer minecraftServer = FMLClientHandler.instance().getServer();
			String allNames[] = minecraftServer.getAllUsernames().clone();
			for(int i = 0; i < allNames.length; i++) {
				EntityPlayerMP player = minecraftServer.getConfigurationManager().getPlayerForUsername(allNames[i]);
				ThirstUtils.readNbt(player.getEntityData());
			}
		} catch(Exception e) {
			//Error.
		}
	}
	
	/**
	 * Called when the player right clicks on a living entity.
	 * @param attack
	 */
	@ForgeSubscribe
	public void onAttack(AttackEntityEvent attack) {
		ThirstUtils.getStats().addExhaustion(0.3f);
	}
	
	/**
	 * Called when the player is damaged. i.e when loses health.
	 * @param hurt
	 */
	@ForgeSubscribe
	public void onHurt(LivingHurtEvent hurt) {
		if(hurt.entityLiving instanceof EntityPlayer) {
			ThirstUtils.getStats().addExhaustion(0.3f);
		}
	}
	
	/** 
	 * Determines if the player is jumping.
	 * @return
	 */
	public static boolean isJumping() {
		return ThirstUtils.getPlayerMp().onGround == false;
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
	 * Gets the client gui.
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

	/**
	 * Called when an item is drunk.
	 */
	@Override
	public boolean onItemDrunk(ItemStack item, int levelAdded, float saturationAdded) {
		if(item.getItem() == Item.potion && item.getItemDamage() == 0) {
			Random rand = new Random(); 
			if(rand.nextFloat() < 0.3f) {
				PoisonController.startPoison();
			}
		}
		return true;
	}

	/**
	 * Called when an item is being drunk. Not used yet...
	 */
	@Override
	public void onItemBeingDrunk(ItemStack item, int timeRemaining) {}
}