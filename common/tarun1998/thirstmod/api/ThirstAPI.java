package tarun1998.thirstmod.api;

public class ThirstAPI {
	
	/**
	 * Register any of your API classes here.
	 * @param api your class that implements something that extends IAPIBase
	 */
	public static void registerHandler(IAPIBase api) {
		if(api instanceof IContentAPI) {
			APIHooks.contentAPI.add((IContentAPI) api);
		}
		
		if(api instanceof IDrinkAPI) {
			APIHooks.drinkAPI.add((IDrinkAPI) api);
		}
		
		if(api instanceof IPoisonAPI) {
			APIHooks.poisonAPI.add((IPoisonAPI) api);
		}
		
		if(api instanceof IRegisterDrink) {
			APIHooks.drinkRegisterAPI.add((IRegisterDrink) api);
		}
		
		if(api instanceof IStatsAPI) {
			APIHooks.statsAPI.add((IStatsAPI) api);
		}
	}
}
