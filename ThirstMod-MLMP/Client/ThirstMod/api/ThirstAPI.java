package net.minecraft.src.ThirstMod.api;

import net.minecraft.src.ThirstMod.core.*;
import net.minecraft.src.*;

public class ThirstAPI
{
	/**
	 * Used to check the current version of ThirstMod.
	 * @return The Version of the mod.
	 */
	public String getVersion()
	{
		return ThirstUtils.getVersion();
	}
	
	/**
	 * Register your class that will be used to edit the ThirstStats.class.
	 * @param api A class that extends ThirstStatsAPI.
	 * @param player EntityPlayer instance. Use ModLoader.getMinecraftInstance.
	 */
	public static void register(ThirstStatsAPI api, EntityPlayer player)
	{
		api.onUpdate(player);
	}
	
	/**
	 * Get an instance of ThirstStats.class.
	 * @return The instance of ThirstStats
	 */
	public static ThirstStats getStats() 
	{
		return ThirstUtils.getStats();
	}
	
	/**
	 * Add a Rain Collector recipe.
	 * @param id shiftedIndex/blockID of fillable item.
	 * @param ret The return of the item.
	 */
	public static void addWCRecipe(int id, ItemStack ret)
	{
		ThirstUtils.addRCRecipe(id, ret);
	}
	
	/**
	 * Add a DrinksBrewer recipe.
	 * @param id The shiftedIndex/blockID of the item to be made to drink.
	 * @param ret The returned object of the item.
	 */
	public static void addJuiceRecipe(int id, ItemStack ret)
	{
		ThirstUtils.addJuiceRecipe(id, ret);
	}
	
	/**
	 * Same as above but with metadata support.
	 * @param id The ID of the item.
	 * @param meta The metadata of the item. 
	 * @param ret The returned object.
	 */
	public static void addJuiceRecipe(int id, int meta, ItemStack ret)
	{
		ThirstUtils.addJuiceRecipe(id, meta, ret);
	}
}
