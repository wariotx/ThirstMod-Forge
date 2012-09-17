package tarun1998.thirstmod.api;

/**
 * If you implement this class, please give appropriate values to boolean,
 * floats and integers, as not doing so can cause the mod to behave incorrectly.
 * For booleans always return true.
 */

public interface IPoisonAPI extends IAPIBase {

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
	 * Called when PoisonController.startPoison is called. If return == true
	 * then the poison will occur else it will not.
	 * @return false to not poison
	 */
	public boolean shouldPoison();
}
