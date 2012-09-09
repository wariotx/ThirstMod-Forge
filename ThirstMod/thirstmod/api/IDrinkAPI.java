package net.minecraft.src.thirstmod.api;

import net.minecraft.src.ItemStack;

/**
 * Provides two events triggered by an item being or finished drinking.
 * You must first register your item with DrinkController.
 * This can be used to add poison when the item is being drunk or even not adding the stats.
 * This can be implemented by a Item class.
 * 
 * If you implement this class, please give appropriate values to boolean, floats and integers, as not doing so can cause the mod
 * to behave incorrectly. For booleans always return true.
 */

public interface IDrinkAPI {
	
	/**
	 * Called when an item is drunk.
	 * @param item The Item being drunk.
	 * @param levelAdded Amount of level to be added.
	 * @param saturationAdded Amount of saturation to be added.
	 * @return false to cancel adding stats.
	 */
	public boolean onItemDrunk(ItemStack item, int levelAdded, float saturationAdded);
	
	/**
	 * Called when an item is being drunk.
	 * This one also returns other items/drinks been drunk that are in the game or made using Custom Drink Creation
	 * @param item The item being drunk
	 * @param timeRemaining Time remaining until onItemDrunk is called. I.E. Finished drinking
	 */
	public void onItemBeingDrunk(ItemStack item, int timeRemaining);
}
