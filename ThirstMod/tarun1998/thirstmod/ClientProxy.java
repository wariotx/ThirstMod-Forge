package tarun1998.thirstmod;

import tarun1998.thirstmod.gui.GuiThirst;
import tarun1998.thirstmod.gui.GuiUpdate;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class ClientProxy extends CommonProxy {
	public boolean loadedMod = false;
	private boolean changedGui = false;
	private boolean isUpdate = false;
	
	@Override
	public void onLoad() {
		MinecraftForgeClient.preloadTexture("/thirstmod/textures/icons.png");
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		
		LanguageRegistry.addName(ThirstMod.waterCollector, "Rain Collector");
		LanguageRegistry.addName(ThirstMod.juiceMaker, "Drinks Brewer");
		LanguageRegistry.addName(ThirstMod.Filter, "Clean Filter");
		LanguageRegistry.addName(ThirstMod.dFilter, "Dirty Filter");
		
		isUpdate = ThirstUtils.checkForUpdate();
		new ContentLoader(FMLClientHandler.instance().getSide());
	}
	
	@Override
	public void onTickInGame() {
		Minecraft minecraft = FMLClientHandler.instance().getClient();
		if(minecraft.currentScreen != null) {
			onTickInGUI(minecraft.currentScreen);
		}
		
		if(getPlayerMp().capabilities.isCreativeMode == false) {
			if(loadedMod == false) {
				if(isUpdate == true) {
					FMLClientHandler.instance().displayGuiScreen(minecraft.thePlayer, new GuiUpdate());
					isUpdate = false;
				}
				if(changedGui == false) {
					minecraft.ingameGUI = new GuiThirst();
					changedGui = true;
				}
				onLoadNBT();
				new ThirstUtils();
				loadedMod = true;
			}
		}
		dc.onTick(minecraft.thePlayer, Side.CLIENT );
		ThirstUtils.getStats().onTick(getPlayer());
	}
	
	public void onTickInGUI(GuiScreen gui) {
		if(gui instanceof GuiMainMenu) {
			ThirstUtils.setDefaults();
			ThirstUtils.setModUnloaded();
		}
		if(gui instanceof GuiGameOver) {
			ThirstUtils.setDefaults();
		}
	}
	
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
	 * Gets an EntityPlayer.class instance.
	 * @return EntityPlayer.class instance.
	 */
	public static EntityPlayer getPlayer() {
		return FMLClientHandler.instance().getClient().thePlayer;
	}
	
	/** 
	 * Gets an EntityPlayerMP.class instance.
	 * @return EntityPlayerMP.class instance.
	 */
	public static EntityPlayer getPlayerMp() {
		try {
			return (EntityPlayerMP) FMLClientHandler.instance().getServer().getConfigurationManager().playerEntityList.iterator().next();
		} catch(Exception e) {
			return FMLClientHandler.instance().getClient().thePlayer;
		}
	}
}
