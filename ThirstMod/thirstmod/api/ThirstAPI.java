package net.minecraft.src.thirstmod.api;

public class ThirstAPI {
	private static ThirstAPI instance;
	public IDrinkAPI[] registeredDrinkAPI = new IDrinkAPI[100];
	public IStatsAPI[] registeredStatsAPI = new IStatsAPI[100];
	public IPoisonAPI[] registeredPoisonAPI = new IPoisonAPI[100];
	public IRegisterDrink[] registeredRegisterAPI = new IRegisterDrink[150];
	public static int idDrink;
	public static int idStats;
	
	public ThirstAPI() {
		instance = this;
	}
	
	public boolean registerAPIDrink(IDrinkAPI drinkAPI) {
		if(registeredDrinkAPI[idDrink] == null) {
			registeredDrinkAPI[idDrink] = drinkAPI;
			idDrink++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerAPIStats(IStatsAPI statsAPI) {
		if(registeredStatsAPI[idStats] == null) {
			registeredStatsAPI[idStats] = statsAPI;
			idStats++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerAPIPoison(IPoisonAPI poisonAPI) {
		if(registeredPoisonAPI[idStats] == null) {
			registeredPoisonAPI[idStats] = poisonAPI;
			idStats++;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean registerAPIDrinkRegister(IRegisterDrink register) {
		if (registeredRegisterAPI[idStats] == null) {
			registeredRegisterAPI[idStats] = register;
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
