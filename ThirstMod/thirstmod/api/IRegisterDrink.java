package net.minecraft.src.thirstmod.api;

import net.minecraft.src.*;
import net.minecraft.src.thirstmod.api.IDrinkAPI;

public interface IRegisterDrink extends IDrinkAPI {
	
	/**
	 * Implement this in your item class and return your item. It will automatically be registered
	 * next time the game is run. You can then use the onItemDrunk and onItemBeingDrunk events.
	 */
	public Item register();
}
