package tarun1998.thirstmod;

import java.util.EnumSet;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;

public class TickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {/*Not Used*/}

	/**
	 * Does the onTickInGame and onTickInGui stuff.
	 */
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		ThirstMod.INSTANCE.onTickInGame();
	}

	@Override
	public EnumSet<TickType> ticks() { 
		if(FMLCommonHandler.instance().getSide() == Side.SERVER) return EnumSet.of(TickType.WORLD);
		else return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() { return null; }
}