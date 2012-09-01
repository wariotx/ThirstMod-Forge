package tarun1998.mods.ThirstMod;

import java.util.EnumSet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.server.FMLServerHandler;

@Mod(modid = "ThirstMod", name = "Thirst Mod", version = "1.1.0")
public class ThirstMod {
	@SidedProxy(clientSide = "tarun1998.mods.ThirstMod.ClientProxy", serverSide = "tarun1998.mods.ThirstMod.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance
	public static ThirstMod INSTANCE;
	
	@Init
	public void onLoad(FMLInitializationEvent event) {
		proxy.onLoad();
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		TickRegistry.registerTickHandler(new TickHandler.TickHandlerServer(), Side.SERVER);
	}
	
	public void onTick() {
		proxy.onTick();
	}
}
