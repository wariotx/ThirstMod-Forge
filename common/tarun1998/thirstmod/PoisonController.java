package tarun1998.thirstmod;

import java.util.*;
import tarun1998.thirstmod.api.*;
import net.minecraftforge.common.MinecraftForge;
import tarun1998.thirstmod.utils.*;

public class PoisonController {
	private static int poisonTimer;
	private static boolean poisonPlayer = false;
	private static boolean isPoisoned = false;
	private static Map biomesList = new HashMap();
	private boolean loadedClass = false;

	/**
	 * PoisonControler ticks. Called from PlayerStatistics.class.
	 */
	public void onTick() {
		if (shouldPoison() == true) {
			poisonPlayer();
		}
		if (loadedClass == false) {
			addBiomePoison();
			loadedClass = true;
		}
	}

	/**
	 * Starts the poison.
	 */
	public static void startPoison() {
		poisonPlayer = true;
	}

	/**
	 * Checks if the game should poison.
	 * @return if can poison.
	 */
	public static boolean shouldPoison() {
		if (poisonPlayer == true && MinecraftForge.EVENT_BUS.post(new ThirstAPI.ShouldPoison()) == false) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Poison the Player.
	 */
	private void poisonPlayer() {
		if (ConfigHelper.poisonOn == true) {
			if(shouldPoison() == true) {
				MinecraftForge.EVENT_BUS.post(new ThirstAPI.OnPlayerPoisoned(360 - poisonTimer));
				poisonTimer++;
				ThirstUtils.getStats().addExhaustion(0.052777777777778f);
				isPoisoned = true;
				if (poisonTimer > 360) {
					poisonTimer = 0;
					isPoisoned = false;
					poisonPlayer = false;
				}
			}
		}
	}

	/**
	 * Checks if the player is currently poisoned.
	 * @return if the player is poisoned.
	 */
	public static boolean isPoisoned() {
		if (isPoisoned == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks how much time is remaining until the poison stops.
	 * @return remaining time until poison stops.
	 */
	public static int poisonTimeRemain() {
		if (poisonTimer > 0) {
			return poisonTimer;
		} else {
			return 0;
		}
	}

	/**
	 * Shows the poison rate for a particular biome. If biome is not found will
	 * return 0.3f or "Other".
	 * @param biome String of biomes name. Available in BiomeGenBase.class.
	 * @return poison rate in float.
	 */
	public static float getBiomePoison(String biome) {
		if (biomesList.containsKey(biome)) {
			return (Float) biomesList.get(biome);
		} else {
			return (Float) biomesList.get("Other");
		}
	}

	/**
	 * Adds a biome and its poison value to the Map.
	 */
	public void addBiomePoison() {
		biomesList.put("Ocean", 0.6f);
		biomesList.put("Plains", 0.3f);
		biomesList.put("Desert", 0.1f);
		biomesList.put("Extreme Hills", 0.2f);
		biomesList.put("Forest", 0.2f);
		biomesList.put("Swampland", 0.8f);
		biomesList.put("FrozenOcean", 0.1f);
		biomesList.put("FrozenRiver", 0.1f);
		biomesList.put("Ice Plains", 0.1f);
		biomesList.put("Ice Mountains", 0.1f);
		biomesList.put("River", 0.2f);
		biomesList.put("Other", 0.3f);
	}

	/**
	 * Get the biomes poison list.
	 * @return the biomes list.
	 */
	public static Map getBiomesList() {
		return biomesList;
	}
	
	public static void setPoisonedTo(boolean what) {
		poisonPlayer = what;
	}
	
	public static void setPoisonTime(int what) {
		poisonTimer = what;
	}
}
