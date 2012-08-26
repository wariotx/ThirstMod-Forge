package net.minecraft.src.thirstmod;

import java.util.EnumSet;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {/*Not Used*/}

	/**
	 * Does the onTickInGame and onTickInGui stuff.
	 */
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(FMLClientHandler.instance().getClient().currentScreen != null) {
			ThirstMod.INSTANCE.onTickInGui(FMLClientHandler.instance().getClient().currentScreen);
		} else if(FMLClientHandler.instance().getClient().thePlayer != null) {
			ThirstMod.INSTANCE.onTickInGame(FMLClientHandler.instance().getClient());
		}
	}

	@Override
	public EnumSet<TickType> ticks() { return EnumSet.of(TickType.CLIENT); }

	@Override
	public String getLabel() { return null; }
}
