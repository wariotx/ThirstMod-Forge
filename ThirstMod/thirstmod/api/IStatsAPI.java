package net.minecraft.src.thirstmod.api;

public interface IStatsAPI {
	
	/**
	 * Called when stats are added using PlayerStatistics.addStats
	 * @param level added
	 * @param saturation added
	 * @return true to add, false to cancel
	 */
	public boolean onStatsAdded(int level, float saturation);
}
