package tarun1998.thirstmod.api;

import net.minecraft.src.*;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.Event;

public class ThirstAPI extends Event {
	
	public static class OnPlayerHurt extends ThirstAPI {
		public EntityPlayer currentPlayer;
		public OnPlayerHurt(EntityPlayer player) {
			currentPlayer = player;
		}
	}
	
	public static class OnPlayerDrinkWaterSource extends ThirstAPI {
		public EntityPlayer currentPlayer;
		public OnPlayerDrinkWaterSource(EntityPlayer player) {
			currentPlayer = player;
		}
	}
	
	public static class OnPlayerPoisoned extends ThirstAPI {
		public int timeRemaining;
		public OnPlayerPoisoned(int i) {
			timeRemaining = i;
		}
	}
	
	@Cancelable
	public static class ShouldPoison extends ThirstAPI {
		public ShouldPoison() {}
	}
}
