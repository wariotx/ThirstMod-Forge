package net.minecraft.src.thirstmod.api;

public class ThirstAPI {
	private static ThirstAPI instance;
	public IDrinkAPI[] registeredDrinkAPI = new IDrinkAPI[100];
	public IStatsAPI[] registeredStatsAPI = new IStatsAPI[100];
	public IContentAPI[] registeredContentAPI = new IContentAPI[100];
	public IPoisonAPI[] registeredPoisonAPI = new IPoisonAPI[100];
	public static int idDrink;
	public static int idStats;
	
	public ThirstAPI() {
		instance = this;
	}
	
	public boolean registerAPI(IDrinkAPI drinkAPI) {
		if(registeredDrinkAPI[idDrink] == null) {
			registeredDrinkAPI[idDrink] = drinkAPI;
			idDrink++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerAPI(IStatsAPI statsAPI) {
		if(registeredStatsAPI[idStats] == null) {
			registeredStatsAPI[idStats] = statsAPI;
			idStats++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerAPI(IContentAPI contentAPI) {
		if(registeredContentAPI[idStats] == null) {
			registeredContentAPI[idStats] = contentAPI;
			idStats++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerAPI(IPoisonAPI poisonAPI) {
		if(registeredPoisonAPI[idStats] == null) {
			registeredPoisonAPI[idStats] = poisonAPI;
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
