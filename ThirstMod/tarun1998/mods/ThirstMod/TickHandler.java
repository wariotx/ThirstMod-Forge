package tarun1998.mods.ThirstMod;

import java.util.EnumSet;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.server.FMLServerHandler;

public class TickHandler implements ITickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		ThirstMod.INSTANCE.onTick();
	}

	@Override
	public EnumSet<TickType> ticks() { 
		return EnumSet.of(TickType.PLAYER);
	}
	

	@Override
	public String getLabel() { return null; }
	
	public static class TickHandlerServer implements ITickHandler {
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) {}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) {
			ThirstMod.INSTANCE.onTick();
		}

		@Override
		public EnumSet<TickType> ticks() { 
			return EnumSet.of(TickType.SERVER);
		}
		

		@Override
		public String getLabel() { return null; }
	}
}
