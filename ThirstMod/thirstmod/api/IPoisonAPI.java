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
}
