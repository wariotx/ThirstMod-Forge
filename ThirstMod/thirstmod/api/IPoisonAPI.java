package net.minecraft.src.thirstmod.api;

public interface IPoisonAPI {
	
	/**
	 * Called when the player is poisoned.
	 * @param timeRemaining until the poison ends.
	 */
	public void onPlayerPoisoned(int timeRemaining);
	
	/**
	 * Called when the poison on a player stops.
	 */
	public void onPoisonStopped();
	
	/**
	 * Return a float which determines a poison chance value for a specific Biome
	 * @param biomeName The name of the biome the player could be in.
	 * @return Poison chance value: 0.1 (0% Chance) to 0.9 (90% Chance)
	 */
	public float getPoisonAmount(String biomeName);
}
