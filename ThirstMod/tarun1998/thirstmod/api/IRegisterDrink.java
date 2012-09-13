package tarun1998.thirstmod.api;

import net.minecraft.src.*;

public interface IRegisterDrink extends IDrinkAPI {
	
	/**
	 * Register your drink with DrinkController. Implementing this will require you to also implement the methods
	 * from IDrinkAPI, making it easier to do stuff.
	 * @return your drink. If implement in Item.class set return "this".
	 */
	public Item register();
}
