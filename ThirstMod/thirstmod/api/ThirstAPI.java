package net.minecraft.src.thirstmod.api;

public class ThirstAPI {
	
	public static void registerAPIDrink(IDrinkAPI drinkAPI) {
		APIHooks.drinkAPI.add(drinkAPI);
	}
	
	public static void registerAPIStats(IStatsAPI statsAPI) {
		APIHooks.statsAPI.add(statsAPI);
	}
	
	public static void registerAPIPoison(IPoisonAPI poisonAPI) {
		APIHooks.poisonAPI.add(poisonAPI);
	}
	
	public static void registerAPIDrinkRegister(IRegisterDrink register) {
		APIHooks.drinkRegisterAPI.add(register);
	}
}
