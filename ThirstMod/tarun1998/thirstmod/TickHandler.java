package tarun1998.thirstmod;

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
		if(FMLClientHandler.instance().getClient().thePlayer != null) {
			ThirstMod.INSTANCE.onTickInGame();
		}
	}

	@Override
	public EnumSet<TickType> ticks() { return EnumSet.of(TickType.CLIENT); }

	@Override
	public String getLabel() { return null; }
}

class TickHanderServer implements ITickHandler {
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
	public EnumSet<TickType> ticks() { return EnumSet.of(TickType.WORLD); }

	@Override
	public String getLabel() { return null; }
}
