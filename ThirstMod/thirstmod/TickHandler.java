package net.minecraft.src.thirstmod;

import java.util.EnumSet;

import javax.jws.Oneway;

import net.minecraft.src.ModLoader;
import net.minecraft.src.ThirstMod;

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
		if(ModLoader.getMinecraftInstance().currentScreen != null) {
			ThirstMod.INSTANCE.onTickInGui(ModLoader.getMinecraftInstance().currentScreen);
		} else if(ModLoader.getMinecraftInstance().thePlayer != null) {
			ThirstMod.INSTANCE.onTickInGame(ModLoader.getMinecraftInstance());
		}
	}

	@Override
	public EnumSet<TickType> ticks() { return EnumSet.of(TickType.CLIENT); }

	@Override
	public String getLabel() { return null; }
}
