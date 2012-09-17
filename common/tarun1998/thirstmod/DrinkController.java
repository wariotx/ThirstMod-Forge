package tarun1998.thirstmod;

import java.util.HashMap;
import java.util.Random;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import tarun1998.thirstmod.api.*;

public class DrinkController {
	private int itemHeal = 0;
	private static HashMap otherMap = new HashMap();
	private static HashMap levelMap = new HashMap();
	private static HashMap saturationMap = new HashMap();

	public void onTick(EntityPlayer player, Side side) {
		// 35 server 38 client for itemInUse field.
		ItemStack item;
		if (side == Side.CLIENT) {
			item = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, player, 38);
		} else {
			item = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, player, 35);
		}
		if (item != null) {
			if (levelMap.containsKey(item.getItem())) {
				onItemBeingDrunk(item);
			}
		} else if (itemHeal > 0 && itemHeal <= 23) {
			itemHeal = 0;
		}

		if (item != null) {
			if (otherMap.containsKey(item.getItem())) {
				onOtherItemBeingDrunk(item);
			}
		} else if (itemHeal > 0 && itemHeal <= 23) {
			itemHeal = 0;
		}
	}

	/**
	 * Called when an item in the levelMap Map is being used/consumed.
	 * Alternative to Reflection used before.
	 * @param item The itemstack being consumed.
	 */
	public void onItemBeingDrunk(ItemStack item) {
		if (levelMap.containsKey(item.getItem())) {
			APIHooks.onItemBeingDrunk(item, item.getMaxItemUseDuration() - itemHeal);
			itemHeal++;
			if (itemHeal == item.getMaxItemUseDuration()) {
				if (APIHooks.onItemDrunk(item, (Integer) levelMap.get(item.getItem()), (Float) saturationMap.get(item.getItem())) == true) {
					ThirstUtils.getStats().addStats((Integer) levelMap.get(item.getItem()), (Float) saturationMap.get(item.getItem()));
				}
				itemHeal = 0;
			}
		}
	}

	public void onOtherItemBeingDrunk(ItemStack item) {
		if (itemHeal == item.getMaxItemUseDuration()) {
			if (APIHooks.onItemDrunk(item, (Integer) levelMap.get(item.getItem()), (Float) saturationMap.get(item.getItem())) == true) {
				ThirstUtils.getStats().addStats((Integer) levelMap.get(item.getItem()), (Float) saturationMap.get(item.getItem()));
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
