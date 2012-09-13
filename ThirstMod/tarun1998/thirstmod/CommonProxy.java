package tarun1998.thirstmod;

import tarun1998.thirstmod.gui.GuiThirst;
import tarun1998.thirstmod.gui.GuiUpdate;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class CommonProxy {
	public DrinkController dc = new DrinkController();
	public boolean loadedMod = false;
	
	public void onLoad() {
		TickRegistry.registerTickHandler(new TickHanderServer(), Side.SERVER);
		new ContentLoader(FMLServerHandler.instance().getSide());
	}
	
	public void onTickInGame() {
		String[] names = FMLServerHandler.instance().getServer().getAllUsernames();
		for(int i = 0; i < names.length; i++) {
			EntityPlayerMP player = FMLServerHandler.instance().getServer().getConfigurationManager().getPlayerForUsername(names[i]);
			if(player.capabilities.isCreativeMode == false) {
				if(loadedMod == false) {
					onLoadNBT();
					loadedMod = true;
				}
			}
			dc.onTick(player, Side.SERVER);
			ThirstUtils.getStats().onTick(player);
		}
	}
	
	public void onLoadNBT() {
		try {
			MinecraftServer minecraftServer = FMLServerHandler.instance().getServer();
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
			MinecraftServer minecraft = FMLServerHandler.instance().getServer();
			String allNames[] = minecraft.getAllUsernames().clone();
			for(int i = 0; i < allNames.length; i++) {
				EntityPlayerMP player = minecraft.getConfigurationManager().getPlayerForUsername(allNames[i]);
				ThirstUtils.writeNbt(player.getEntityData());
			}
		} catch(Exception e) {
		}
 	}
}
