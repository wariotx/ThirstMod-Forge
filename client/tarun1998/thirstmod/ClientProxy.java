package tarun1998.thirstmod;

import tarun1998.thirstmod.blocks.*;
import tarun1998.thirstmod.gui.*;
import tarun1998.thirstmod.packets.PacketHandleSave;
import tarun1998.thirstmod.utils.*;
import cpw.mods.fml.client.*;
import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.client.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.world.WorldEvent;

public class ClientProxy extends CommonProxy {
	public boolean loadedMod = false;
	private boolean changedGui = false;
	private int intDat;

	@Override
	public void onLoad() {
		MinecraftForgeClient.preloadTexture("/tarun1998/thirstmod/textures/icons.png");
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);

		LanguageRegistry.addName(ThirstMod.waterCollector, "Rain Collector");
		LanguageRegistry.addName(ThirstMod.juiceMaker, "Drinks Brewer");
		LanguageRegistry.addName(ThirstMod.Filter, "Clean Filter");
		LanguageRegistry.addName(ThirstMod.dFilter, "Dirty Filter");
		GameRegistry.registerTileEntity(TileEntityJM.class, "DrinksBrewer");
		GameRegistry.registerTileEntity(TileEntityRC.class, "WaterCollector");

		new ContentLoader(FMLClientHandler.instance().getSide());
		KeyBindingRegistry.registerKeyBinding(new KeyHandler());
	}

	@Override
	public void onTickInGame() {
		Minecraft minecraft = FMLClientHandler.instance().getClient();
		if (minecraft.currentScreen != null) {
			onTickInGUI(minecraft.currentScreen);
		}

		if (minecraft.thePlayer != null) {
			if (getPlayer().capabilities.isCreativeMode == false) {
				if (loadedMod == false) {
					if (changedGui == false) {
						minecraft.ingameGUI = new GuiThirst();
						changedGui = true;
					}

					new ThirstUtils();
					onLoadNBT();
					loadedMod = true;
				}
			}
			ThirstUtils.getStats().onTick(getPlayer());

			intDat++;
			if (PacketHandler.isRemote == true & intDat > 20) {
				PacketHandleSave.sendSaveData(getPlayer().username, ThirstUtils.getStats());
			}
		}
	}

	public void onTickInGUI(GuiScreen gui) {
		if (gui instanceof GuiMainMenu) {
			ThirstUtils.setDefaults();
			loadedMod = false;
		}
		if (gui instanceof GuiGameOver) {
			ThirstUtils.setDefaults();
		}
	}

	public void onLoadNBT() {
		try {
			MinecraftServer minecraftServer = FMLClientHandler.instance().getServer();
			String allNames[] = minecraftServer.getAllUsernames().clone();
			for (int i = 0; i < allNames.length; i++) {
				EntityPlayerMP player = minecraftServer.getConfigurationManager().getPlayerForUsername(allNames[i]);
				ThirstUtils.readNbt(player.getEntityData());
			}
		} catch (Exception e) {
			/* This shouldn't be called at all! It is impossible */
		}
	}

	@ForgeSubscribe
	public void onSave(WorldEvent.Save save) {
		try {
			MinecraftServer minecraft = FMLClientHandler.instance().getServer();
			String allNames[] = minecraft.getAllUsernames().clone();
			for (int i = 0; i < allNames.length; i++) {
				EntityPlayerMP player = minecraft.getConfigurationManager().getPlayerForUsername(allNames[i]);
				ThirstUtils.writeNbt(player.getEntityData());
			}
		} catch (Exception e) {
			/* This shouldn't be called at all! It is impossible */
		}
	}

	/**
	 * Gets an EntityPlayer.class instance.
	 * @return EntityPlayer.class instance.
	 */
	public static EntityPlayer getPlayer() {
		return FMLClientHandler.instance().getClient().thePlayer;
	}

	/**
	 * Gets an EntityPlayerMP.class instance. Only works on client and not connected to another server.
	 * @return EntityPlayerMP.class instance.
	 */
	public static EntityPlayerMP getPlayerMp() {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		EntityPlayerMP player = server.getConfigurationManager().getPlayerForUsername(getPlayer().username);
		return player;
	}
}