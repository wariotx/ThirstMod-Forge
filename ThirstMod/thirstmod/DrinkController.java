package net.minecraft.src.thirstmod;

import java.util.HashMap;
import java.util.Random;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class DrinkController {
	private int itemHeal = 0;
	private static HashMap levelMap = new HashMap();
	private static HashMap saturationMap = new HashMap();
	private static HashMap poisonMap = new HashMap();
	
	public void onTick(Minecraft minecraft) {
		ItemStack item = ObfuscationReflectionHelper.getPrivateValue(EntityPlayer.class, minecraft.thePlayer, "itemInUse");
		if(item != null) {
			if(levelMap.containsKey(item.getItem())) {
				onItemBeingDrunk(item);
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
			itemHeal++;
			if(itemHeal == 24) {
				ThirstUtils.getStats().addStats((Integer)levelMap.get(item.getItem()), (Float)saturationMap.get(item.getItem()));
				if(item.getItem() == Item.potion && item.getItemDamage() == 0) {
					Random rand = new Random(); 
					if(rand.nextFloat() < 0.3f) {
						PoisonController.startPoison();
						System.out.println("Poison cause you were drinking bad water.");
					}
				}
				itemHeal = 0;
			}
		}
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
}
