package tarun1998.mods.ThirstMod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import cpw.mods.fml.server.FMLServerHandler;

public class CommonProxy {
	
	public void onLoad() {
	}
	
	public void onTick() {
		MinecraftServer server = FMLServerHandler.instance().getServer();
		
		String[] names = server.getAllUsernames();
		for(int i = 0; i < names.length; i++) {
			EntityPlayerMP player = server.getConfigurationManager().getPlayerForUsername(names[i]);
			if (player != null) {
				
			}
		}
	}
}
