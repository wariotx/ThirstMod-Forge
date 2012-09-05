package net.minecraft.src.thirstmod.api;

public class ThirstAPI {
	private static ThirstAPI instance;
	public IDrinkAPI[] registeredDrinkAPI = new IDrinkAPI[100];
	public IStatsAPI[] registeredStatsAPI = new IStatsAPI[100];
	public static int idDrink;
	public static int idStats;
	
	public ThirstAPI() {
		instance = this;
	}
	
	public boolean registerDrinkAPI(IDrinkAPI drinkAPI) {
		if(registeredDrinkAPI[idDrink] == null) {
			registeredDrinkAPI[idDrink] = drinkAPI;
			idDrink++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerStatsAPI(IStatsAPI statsAPI) {
		if(registeredStatsAPI[idStats] == null) {
			registeredStatsAPI[idStats] = statsAPI;
			idStats++;
			return true;
		} else {
			return false;
		}
	}
	
	public static ThirstAPI instance() {
		return instance;
	}
}
