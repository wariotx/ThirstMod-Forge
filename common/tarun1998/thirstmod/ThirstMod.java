package tarun1998.thirstmod;

import java.util.Random;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.src.*;
import tarun1998.thirstmod.api.*;
import tarun1998.thirstmod.blocks.*;
import tarun1998.thirstmod.gui.*;
import tarun1998.thirstmod.packets.*;
import tarun1998.thirstmod.utils.*;
import tarun1998.thirstmod.reflection.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.*;

@Mod(modid = ThirstUtils.ID, name = ThirstUtils.NAME, version = ThirstUtils.VERSION)
@NetworkMod(serverSideRequired = false, clientSideRequired = true, packetHandler = PacketHandler.class, channels = { "ThirstMod" })
public class ThirstMod implements IGuiHandler {
	public static final Block waterCollector = new BlockRC(ConfigHelper.rcId).setBlockName("waterCollector").setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabDeco);
	public static final Block juiceMaker = new BlockJM(ConfigHelper.jmId).setBlockName("juiceMaker").setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabDeco);
	public static final Item dFilter = (new ItemThirst(ConfigHelper.dFilterId).setItemName("dFilter").setMaxStackSize(1)).setIconIndex(33).setTabToDisplayOn(CreativeTabs.tabMisc);
	public static final Item Filter = (new ItemThirst(ConfigHelper.filterId).setItemName("filter").setMaxStackSize(1)).setContainerItem(dFilter).setIconIndex(32).setTabToDisplayOn(CreativeTabs.tabMisc);

	public static int jmFront = 1;
	public static int rcTop = 0;

	@Instance
	public static ThirstMod INSTANCE;

	@SidedProxy(clientSide = "tarun1998.thirstmod.ClientProxy", serverSide = "tarun1998.thirstmod.CommonProxy")
	public static CommonProxy proxy;

	public static boolean modOff = false;

	public static boolean shouldTellPlayer0 = false;
	public static boolean shouldTellPlayer1 = false;
	
	//Networks
	public PacketHandleSave savePacket = new PacketHandleSave();
	public PacketPlaySound soundPacket = new PacketPlaySound();
	public PacketPlayerPos posPacket = new  PacketPlayerPos();

	/**
	 * Called when the mod has been loaded.
	 * @param event
	 */
	@Init
	public void onLoad(FMLInitializationEvent event) {
		GameRegistry.registerBlock(waterCollector);
		GameRegistry.registerBlock(juiceMaker);

		GameRegistry.registerTileEntity(TileEntityRC.class, "Rain Collector");
		GameRegistry.registerTileEntity(TileEntityJM.class, "Juice Maker");

		GameRegistry.addRecipe(new ItemStack(waterCollector, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.bucketEmpty, });

		GameRegistry.addRecipe(new ItemStack(juiceMaker, 1), new Object[]
		{ "***", "*#*", "***", Character.valueOf('*'), Block.cobblestone, Character.valueOf('#'), Item.glassBottle, });

		GameRegistry.addRecipe(new ItemStack(Filter), new Object[]
		{ "***", "*!*", "***", Character.valueOf('*'), Block.planks, Character.valueOf('!'), Item.silk });

		GameRegistry.addShapelessRecipe(new ItemStack(Filter), new Object[]
		{ Item.silk, dFilter });

		new ConfigHelper();
		new DrinkLoader().loadDrinks();

		MinecraftForge.EVENT_BUS.register(INSTANCE);
		MinecraftForge.EVENT_BUS.register(proxy);
		NetworkRegistry.instance().registerGuiHandler(this, this);
		NetworkRegistry.instance().registerConnectionHandler(savePacket);
		
		replaceItems();

		proxy.onLoad();
	}
	
	@PostInit
	public void modsLoaded(FMLPostInitializationEvent event) {
	}

	/**
	 * Called when 1 game loop is done.
	 * @param minecraft
	 */
	public void onTickInGame() {
		if (modOff == false) {
			proxy.onTickInGame();
		}
	}

	/**
	 * Called when the player right clicks on a living entity.
	 * @param attack
	 */
	@ForgeSubscribe
	public void onAttack(AttackEntityEvent attack) {
		if(!attack.entity.worldObj.isRemote) {
			ThirstUtils.getStats().addExhaustion(0.6f);
		}
	}

	/**
	 * Called when the player is damaged. i.e when loses health.
	 * @param hurt
	 */
	@ForgeSubscribe
	public void onHurt(LivingHurtEvent hurt) {
		if(!hurt.entity.worldObj.isRemote) {
			if (hurt.entityLiving instanceof EntityPlayer) {
				ThirstUtils.getStats().addExhaustion(0.6f);
			}
		}
	}

	/**
	 * Determines if the player is jumping.
	 * @return
	 */
	public static boolean isJumping(EntityPlayer player) {
		return player.onGround == false;
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
		switch (ID) {
		case 90:
			return new ContainerJM(player.inventory, (TileEntityJM) tile);
		case 91:
			return new ContainerRC(player.inventory, (TileEntityRC) tile);
		}
		return null;
	}

	/**
	 * Gets the client gui.
	 */
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		switch (ID) {
		case 90:
			return new GuiJM(player.inventory, (TileEntityJM) tile);
		case 91:
			return new GuiRC(player.inventory, (TileEntityRC) tile);
		}
		return null;
	}
	
	public void replaceItems() {
		/* Item index's.
		 * Potion = 118
		 * Milk Bucket = 80
		 * Soup = 27
		for(int i = 0; i < Item.class.getFields().length; i++) {
			System.out.printf("Item: %s, ID: %d\n", Item.class.getFields()[i], i);
		}
		*/
		
		Item.itemsList[Item.potion.shiftedIndex] = null;
		Item.itemsList[Item.bucketMilk.shiftedIndex] = null;
		Item.itemsList[Item.bowlSoup.shiftedIndex] = null;
		
		ItemPotion potion = (ItemPotionMod)(new ItemPotionMod(117)).setIconCoord(13, 8).setItemName("potion");
		Item bucketMilk = (new ItemMilkMod(79)).setIconCoord(13, 4).setItemName("milk").setContainerItem(Item.bucketEmpty);
		Item soup = (new ItemSoupMod(26, 8)).setIconCoord(8, 4).setItemName("mushroomStew");
		
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Item.potion, potion, 118);
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Item.bucketMilk, bucketMilk, 80);
		ObfuscationReflectionHelper.setPrivateValue(Item.class, Item.bowlSoup, soup, 27);
	}
}