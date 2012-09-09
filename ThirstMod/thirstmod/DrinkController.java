package net.minecraft.src.thirstmod;

import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.api.APIHooks;
import net.minecraft.src.thirstmod.api.ThirstAPI;

public class DrinkController {
	private int itemHeal = 0;
	private static HashMap otherMap = new HashMap();
	private static HashMap levelMap = new HashMap();
	private static HashMap saturationMap = new HashMap();
	
	public void onTick(Minecraft minecraft) {
		ItemStack item = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, minecraft.thePlayer, 38);
		if(item != null) {
			if(levelMap.containsKey(item.getItem())) {
				onItemBeingDrunk(item);
			}
		} else if(itemHeal > 0 && itemHeal <= 23) {
			itemHeal = 0;
		}
		
		if(item != null) {
			if(otherMap.containsKey(item.getItem())) {
				onOtherItemBeingDrunk(item);
			}
		} else if(itemHeal > 0 && itemHeal <= 23) {
			itemHeal = 0;
		}
	}
	
	/**
	 * Called when an item in the levelMap Map is being used/consumed. Alternative to Reflection used before.
	 * @param item The itemstack being consumed.
	 */
	public void onItemBeingDrunk(ItemStack item) {
		if(levelMap.containsKey(item.getItem())) {
			APIHooks.onItemBeingDrunk(item, item.getMaxItemUseDuration() - itemHeal);
			itemHeal++;
			if(itemHeal == item.getMaxItemUseDuration()) {
				if(APIHooks.onItemDrunk(item, (Integer)levelMap.get(item.getItem()), (Float)saturationMap.get(item.getItem())) == true) {
					ThirstUtils.getStats().addStats((Integer)levelMap.get(item.getItem()), (Float)saturationMap.get(item.getItem()));
				}
				itemHeal = 0;
			}
		}
	}
	
	public void onOtherItemBeingDrunk(ItemStack item) {
		if(itemHeal == item.getMaxItemUseDuration()) {
			if(APIHooks.onItemDrunk(item, (Integer)levelMap.get(item.getItem()), (Float)saturationMap.get(item.getItem())) == true) {
				ThirstUtils.getStats().addStats((Integer)levelMap.get(item.getItem()), (Float)saturationMap.get(item.getItem()));
			}
		}
		itemHeal = 0;
	}
	
	/**
	 * Adds a item to the list. Must already be using EnumAction.drink
	 * @param item The item
	 * @param level amount of thirst heal.
	 * @param saturation amount of saturation heal.
	 */
	public static void addDrink(Item item, int level, float saturation) {
		levelMap.put(item, level);
		saturationMap.put(item, saturation);
	}
	
	public static void addOtherDrink(Item item) {
		otherMap.put(item, 0);
	}
}
