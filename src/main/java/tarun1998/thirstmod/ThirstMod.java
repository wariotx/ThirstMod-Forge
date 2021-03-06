package tarun1998.thirstmod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import tarun1998.thirstmod.blocks.*;
import tarun1998.thirstmod.gui.GuiFilter;
import tarun1998.thirstmod.gui.GuiJM;
import tarun1998.thirstmod.gui.GuiRC;
import tarun1998.thirstmod.packets.PacketHandleSave;
import tarun1998.thirstmod.packets.PacketPlaySound;
import tarun1998.thirstmod.packets.PacketPlayerPos;
import tarun1998.thirstmod.reflection.ItemMilkMod;
import tarun1998.thirstmod.reflection.ItemPotionMod;
import tarun1998.thirstmod.reflection.ItemSoupMod;
import tarun1998.thirstmod.utils.ThirstUtils;

@Mod(modid = ThirstUtils.ID, name = ThirstUtils.NAME, version = ThirstUtils.VERSION)
//@NetworkMod(serverSideRequired = false, clientSideRequired = true, packetHandler = PacketHandler.class, channels = {"ThirstMod"})
public class ThirstMod implements IGuiHandler {
    public static final Block waterCollector = new BlockRC(ConfigHelper.rcId)
            .setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabDeco);

    public static final Block juiceMaker = new BlockJM(ConfigHelper.jmId)
            .setResistance(5F).setHardness(4F).setCreativeTab(CreativeTabs.tabDeco);
    //public static final Block filterBlock = new BlockFilter(1090).setBlockName("filterBlock").setResistance(5f).setHardness(4f).setCreativeTab(CreativeTabs.tabDeco);
    public static final Item dFilter = (new ItemThirst(ConfigHelper.dFilterId)
            .setMaxStackSize(1)).setIconIndex(33).setTabToDisplayOn(CreativeTabs.tabMisc);
    public static final Item Filter = (new ItemThirst(ConfigHelper.filterId)
            .setMaxStackSize(1)).setContainerItem(dFilter).setIconIndex(32).setTabToDisplayOn(CreativeTabs.tabMisc);

    public static int jmFront = 1;
    public static int rcTop = 0;

    @Instance(ThirstUtils.ID)
    public static ThirstMod INSTANCE;

    @SidedProxy(clientSide = "tarun1998.thirstmod.ClientProxy", serverSide = "tarun1998.thirstmod.CommonProxy")
    public static CommonProxy proxy;

    public static boolean modOff = false;

    public static boolean shouldTellPlayer0 = false;
    public static boolean shouldTellPlayer1 = false;

    //Networks
    public PacketHandleSave savePacket = new PacketHandleSave();
    public PacketPlaySound soundPacket = new PacketPlaySound();
    public PacketPlayerPos posPacket = new PacketPlayerPos();

    /**
     * Called when the mod has been loaded.
     *
     * @param event
     */
    @Mod.EventHandler
    public void onLoad(FMLInitializationEvent event) {
        GameRegistry.registerBlock(waterCollector, "waterCollector");
        GameRegistry.registerBlock(juiceMaker, "juiceMaker");
        GameRegistry.registerItem(dFilter, "dFilter");
        GameRegistry.registerItem(Filter, "filter");
        //GameRegistry.registerBlock(filterBlock);

        GameRegistry.registerTileEntity(TileEntityRC.class, "Rain Collector");
        GameRegistry.registerTileEntity(TileEntityJM.class, "Juice Maker");
        //GameRegistry.registerTileEntity(TileEntityFilter.class, "Filter Block");

        GameRegistry.addRecipe(new ItemStack(waterCollector, 1), new Object[]
                {"***", "*#*", "***", Character.valueOf('*'), Blocks.cobblestone, Character.valueOf('#'), Items.bucketEmpty,});

        GameRegistry.addRecipe(new ItemStack(juiceMaker, 1), new Object[]
                {"***", "*#*", "***", Character.valueOf('*'), Blocks.cobblestone, Character.valueOf('#'), Items.glassBottle,});

        GameRegistry.addRecipe(new ItemStack(Filter), new Object[]
                {"***", "*!*", "***", Character.valueOf('*'), Blocks.planks, Character.valueOf('!'), Item.silk});

        GameRegistry.addShapelessRecipe(new ItemStack(Filter), new Object[]
                {Item.silk, dFilter});

        new ConfigHelper();
        new DrinkLoader().loadDrinks();

        MinecraftForge.EVENT_BUS.register(INSTANCE);
        MinecraftForge.EVENT_BUS.register(proxy);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this);
        //TODO Use cpw.mods.fml.common.network.FMLNetworkEvent
//        NetworkRegistry.INSTANCE.registerConnectionHandler(savePacket);

        replaceItems();

        proxy.onLoad();
    }

    public void modsLoaded(FMLPostInitializationEvent event) {
    }

    /**
     * Called when 1 game loop is done.
     *
     * @param minecraft
     */
    public void onTickInGame() {
        if (modOff == false) {
            proxy.onTickInGame();
        }
    }

    /**
     * Called when the player right clicks on a living entity.
     *
     * @param attack
     */
    @ForgeSubscribe
    public void onAttack(AttackEntityEvent attack) {
        if (!attack.entity.worldObj.isRemote) {
            ThirstUtils.getUtilsFor(attack.entityPlayer.username).addExhaustion(0.6f);
        }
    }

    /**
     * Called when the player is damaged. i.e when loses health.
     *
     * @param hurt
     */
    @ForgeSubscribe
    public void onHurt(LivingHurtEvent hurt) {
        if (!hurt.entity.worldObj.isRemote) {
            if (hurt.entityLiving instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) hurt.entityLiving;
                player.addExhaustion(0.6f);
                ThirstUtils.getUtilsFor(player.getDisplayName()).addExhaustion(0.6f);
            }
        }
    }

    /**
     * Determines if the player is jumping.
     *
     * @return
     */
    public static boolean isJumping(EntityPlayer player) {
        return player.onGround == false;
    }

    /**
     * Sets an icon index for an item.
     *
     * @param item The Item
     * @param i    number to set it to.
     */
    public static void setIcon(Item item, int i) {
        item.setIconIndex(i);
    }

    /**
     * Gets the server container.
     */
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        switch (ID) {
            case 90:
                return new ContainerJM(player.inventory, (TileEntityJM) tile);
            case 91:
                return new ContainerRC(player.inventory, (TileEntityRC) tile);
            case 92:
                return new ContainerFilter(player.inventory, (TileEntityFilter) tile);
        }
        return null;
    }

    /**
     * Gets the client gui.
     */
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        switch (ID) {
            case 90:
                return new GuiJM(player.inventory, (TileEntityJM) tile);
            case 91:
                return new GuiRC(player.inventory, (TileEntityRC) tile);
            case 92:
                return new GuiFilter(player.inventory, (TileEntityFilter) tile);
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

        Item.itemsList[Items.potionitem.shiftedIndex] = null;
        Item.itemsList[Items.milk_bucket.shiftedIndex] = null;
        Item.itemsList[Items.bowl.shiftedIndex] = null;

        ItemPotion potion = (ItemPotionMod) (new ItemPotionMod()).setIconCoord(13, 8).setItemName("potion");
        Item bucketMilk = (new ItemMilkMod(79)).setIconCoord(13, 4).setItemName("milk").setContainerItem(Items.bucket);
        Item soup = (new ItemSoupMod(26, 8)).setIconCoord(8, 4).setItemName("mushroomStew");

        ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.potionitem, potion, 118);
        ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.milk_bucket, bucketMilk, 80);
        ObfuscationReflectionHelper.setPrivateValue(Item.class, Items.bowl, soup, 27);
    }
}