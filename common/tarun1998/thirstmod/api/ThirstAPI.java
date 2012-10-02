package tarun1998.thirstmod.api;

import net.minecraft.src.*;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

public class ThirstAPI extends Event {
	/**
	 * Called when the player is hurt because the thirst bar is empty.
	 */
	public static class OnPlayerHurt extends ThirstAPI {
		public EntityPlayer currentPlayer;
		public OnPlayerHurt(EntityPlayer player) {
			currentPlayer = player;
		}
	}
	
	/**
	 * Called when the player drinks from a water source on the ground by
	 * shifting in it or by shifting when it is raining.
	 */
	public static class OnPlayerDrinkWater extends ThirstAPI {
		public EntityPlayer currentPlayer;
		public OnPlayerDrinkWater(EntityPlayer player) {
			currentPlayer = player;
		}
	}
	
	/**
	 * Called when the player is poisoned. This is not called on server side.
	 */
	public static class OnPlayerPoisoned extends ThirstAPI {
		public int timeRemaining;
		public OnPlayerPoisoned(int i) {
			timeRemaining = i;
		}
	}
	
	/**
	 * Called when the player is about to be poisoned. Set canceled to not poison the player.
	 */
	@Cancelable
	public static class ShouldPoison extends ThirstAPI {
		public ShouldPoison() {}
	}
}
