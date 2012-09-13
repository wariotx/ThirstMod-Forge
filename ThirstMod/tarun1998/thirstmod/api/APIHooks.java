package tarun1998.thirstmod.api;

import java.util.LinkedList;

import tarun1998.thirstmod.DrinkController;

import net.minecraft.src.ItemStack;

public class APIHooks {
	public static LinkedList<IDrinkAPI> drinkAPI = new LinkedList<IDrinkAPI>();
	public static LinkedList<IStatsAPI> statsAPI = new LinkedList<IStatsAPI>();
	public static LinkedList<IPoisonAPI> poisonAPI = new LinkedList<IPoisonAPI>();
	public static LinkedList<IRegisterDrink> drinkRegisterAPI = new LinkedList<IRegisterDrink>();
	public static LinkedList<IContentAPI> contentAPI = new LinkedList<IContentAPI>();
	
	public static boolean onItemDrunk(ItemStack item, int i, float f) {
		for(IDrinkAPI drink : drinkAPI) {
			if(drink.onItemDrunk(item, i, f) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static void onItemBeingDrunk(ItemStack item, int i) {
		for(IDrinkAPI drink : drinkAPI) {
			drink.onItemBeingDrunk(item, i);
		}
	}
	
	public static void onPoisonStopped() {
		for(IPoisonAPI poison: poisonAPI) {
			poison.onPoisonStopped();
		}
	}
	
	public static void onPlayerPoisoned(int i) {
		for(IPoisonAPI poison: poisonAPI) {
			poison.onPlayerPoisoned(i);
		}
	}
	
	public static boolean shouldPoison() {
		for(IPoisonAPI poison: poisonAPI) {
			if(poison.shouldPoison() == false) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean onAddStats(int i, float f) {
		for(IStatsAPI stats: statsAPI) {
			if(stats.onStatsAdded(i, f) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static void onExhaust(int i, float f, int j) {
		for(IStatsAPI stats: statsAPI) {
			stats.onAddExhaustion(i, f, j);
		}
	}
	
	public static void onPlayerHurtFromThirst() {
		for(IStatsAPI stats: statsAPI) {
			stats.onPlayerHurtFromDehydration();
		}
	}
	
	public static boolean onPlayerDrink() {
		for(IStatsAPI stats: statsAPI) {
			if(stats.onPlayerDrink() == false) {
				return false;
			}
		}
		return true;
	}
	
	public static void registerDrinks() {
		for(IRegisterDrink drink: drinkRegisterAPI) {
			DrinkController.addOtherDrink(drink.register());
		}
	}
	
	public static void onRead(String s, String[] s1, Class c) {
		for(IContentAPI content: contentAPI) {
			content.onReadFile(s, s1, c);
		}
	}
}
