package tarun1998.thirstmod;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class CommonProxy {
	public boolean loadedMod = false;

	public void onLoad() {
		TickRegistry.registerTickHandler(new TickHanderServer(), Side.SERVER);
		new ContentLoader(FMLServerHandler.instance().getSide());
	}

	public void onTickInGame() {
		String[] names = FMLServerHandler.instance().getServer().getAllUsernames();
		for (int i = 0; i < names.length; i++) {
			EntityPlayerMP player = FMLServerHandler.instance().getServer().getConfigurationManager().getPlayerForUsername(names[i]);
			if (player.capabilities.isCreativeMode == false) {
				getStatsMP().onTick(player, player);
			}
		}
	}
	
	public static PlayerStatisticsMP getStatsMP() {
		return ThirstUtils.statsMp;
	}
}
