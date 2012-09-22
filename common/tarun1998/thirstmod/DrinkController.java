package tarun1998.thirstmod;

import java.util.HashMap;
import java.util.Random;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class DrinkController {
	private static HashMap levelMap = new HashMap();
	private static HashMap saturationMap = new HashMap();

	public void onTick(EntityPlayer player, Side side) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			ItemStack item = player.getItemInUse();
			
			if (item != null) {
				if (levelMap.containsKey(item.getItem())) {
					onItemBeingDrunk(item, player);
				}
			} 
		}
	}

	/**
	 * Called when an item in the levelMap Map is being used/consumed.
	 * Alternative to Reflection used before.
	 * @param item The itemstack being consumed.
	 */
	public void onItemBeingDrunk(ItemStack item, EntityPlayer player) {
		if (levelMap.containsKey(item.getItem())) {
			if(player.getItemInUseCount() == 0) {
				ThirstUtils.getStats().addStats((Integer) levelMap.get(item.getItem()), (Float) saturationMap.get(item.getItem()));
				
				if (item.getItem() == Item.potion && item.getItemDamage() == 0) {
					Random rand = new Random();
					if (rand.nextFloat() < 0.3f) {
						PoisonController.startPoison();
					}
				}
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
