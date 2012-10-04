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
	public boolean loadedMod;
	private boolean changedGui;
	private int intDat;
	private boolean ranGame;
	private String sessionPlayer; // Can only be used once the player has been in a game.

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
				ThirstUtils.addNewPlayer(getPlayer().username, new ThirstUtils());
				if (loadedMod == false) {
					if (changedGui == false) {
						minecraft.ingameGUI = new GuiThirst();
						changedGui = true;
					}

					sessionPlayer = getPlayer().username;
					loadedMod = true;
				}
				ranGame = true;
			}
			ThirstUtils.getUtilsFor(getPlayer().username).getStats().onTick(getPlayer());

			intDat++;
			if (PacketHandler.isRemote == true & intDat > 20) {
				PacketHandleSave.sendSaveData(getPlayer().username, ThirstUtils.getUtilsFor(getPlayer().username).getStats());
			}
			
			if(ThirstUtils.isClientHost() == true) {
				String usernames[] = FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames().clone();
				for(int i = 0; i < usernames.length; i++) {
					MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
					EntityPlayerMP player = server.getConfigurationManager().getPlayerForUsername(usernames[i]);
					if(!PacketHandler.playerInstance.containsKey(player.username)) {
						ThirstUtils.addNewPlayer(player.username, new ThirstUtils());
					}
					this.getStatsMP(usernames[i]).onTick(player, player);
				}
			}
		}
	}

	public void onTickInGUI(GuiScreen gui) {
		if (gui instanceof GuiMainMenu) {
			if(ranGame == true) {
				ThirstUtils.getUtilsFor(sessionPlayer).setDefaults();
			}
			loadedMod = false;
		}
		if (gui instanceof GuiGameOver) {
			ThirstUtils.getUtilsFor(sessionPlayer).setDefaults();
		}
	}

	/**
	 * Writes all the data in PlayerStatistics to the NBT.
	 * @param nbt Player NBT.
	 */
	public static void writeNbt(EntityPlayer player, NBTTagCompound nbt) {
		ThirstUtils utils = (ThirstUtils) PacketHandler.playerInstance.get(player.username);
		PlayerStatistics stats = utils.getStats();
		nbt.setInteger("tmLevel", stats.level);
		nbt.setFloat("tmExhaustion", stats.exhaustion);
		nbt.setFloat("tmSaturation", stats.saturation);
		nbt.setInteger("tmTimer", stats.healhurtTimer);
		nbt.setInteger("tmTimer2", stats.drinkTimer);
		nbt.setBoolean("tmPoisoned", utils.getStats().getPoison().isPoisoned());
		nbt.setInteger("tmPoisonTime", utils.getStats().getPoison().poisonTimeRemain());
	}
	
	/**
	 * Reads the data from the nbt and applies it to the data in
	 * PlayerStatistics.class.
	 * @param nbt Player NBT
	 */
	public static void readNbt(EntityPlayer player, NBTTagCompound nbt) {
		ThirstUtils utils = ThirstUtils.getUtilsFor(player.username);
		PlayerStatistics stats = utils.getStats();
		if (nbt.hasKey("tmLevel")) {
			stats.level = nbt.getInteger("tmLevel");
			stats.exhaustion = nbt.getFloat("tmExhaustion");
			stats.saturation = nbt.getFloat("tmSaturation");
			stats.healhurtTimer = nbt.getInteger("tmTimer");
			stats.drinkTimer = nbt.getInteger("tmTimer2");
			stats.getPoison().setPoisonedTo(nbt.getBoolean("tmPoisoned"));
			stats.getPoison().setPoisonTime(nbt.getInteger("tmPoisonTime"));
		} else {
			utils.setDefaults();
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