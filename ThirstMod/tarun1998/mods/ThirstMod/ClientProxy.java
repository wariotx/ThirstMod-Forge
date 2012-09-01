package tarun1998.mods.ThirstMod;

import net.minecraft.src.GuiScreen;
import cpw.mods.fml.client.FMLClientHandler;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void onLoad() {
		
	}
	
	@Override
	public void onTick() {
		if(FMLClientHandler.instance().getClient().currentScreen != null) {
			onTickInGui(FMLClientHandler.instance().getClient().currentScreen);
		}
	}
	
	public void onTickInGui(GuiScreen gui) {
		
	}
}
