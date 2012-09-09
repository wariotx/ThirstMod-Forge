package net.minecraft.src.thirstmod.api;

/**
 * Provides a couple of events triggered by PlayerStatistics.class.
 * If you implement this class, please give appropriate values to boolean, floats and integers, as not doing so can cause the mod
 * to behave incorrectly. For booleans always return true.
 */

public interface IStatsAPI {
	
	/**
	 * Called when stats are added using PlayerStatistics.addStats
	 * @param level added
	 * @param saturation added
	 * @return true to not add stats
	 */
	public boolean onStatsAdded(int level, float saturation);
	
	/**
	 * Called when PlayerStatistic.exhaustPlayer is called.
	 * @param playerSpeed The speed the player is moving in. In Blocks.
	 * @param speedMultiplier The multiplier of the exhaust speed. From ConfigHelper.thirstRate
	 * @param exhaustionMuliplier The exhaustion multiplier if player is in desert.
	 */
	public void onAddExhaustion(int playerSpeed, float speedMultiplier, int exhaustionMuliplier);
	
	/**
	 * Called when the player is hurt because the thirst bar is empty.
	 */
	public void onPlayerHurtFromDehydration();
	
	/**
	 * Called when the player drinks from a water source on ground. I.E. Shifting in water.
	 * @return false to not add stats.
	 */
	public boolean onPlayerDrink();
}
