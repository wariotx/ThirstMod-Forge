/**
 * ThirstMods Drink class. Similar to ItemFood.
 */
package tarun1998.thirstmod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import net.minecraft.src.*;

public class Drink extends Item {
	private boolean alwaysDrinkable;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;
	private boolean hasEffect = false;
	private Random rand = new Random();
	private String texture;
	private Item returnItem = Item.glassBottle;

	private float poisonChance;

	private int foodHeal;
	private float satHeal;

	public Drink(int id, boolean alwaysDrinkable) {
		super(id);
		if (alwaysDrinkable == true) {
			this.alwaysDrinkable = true;
		}
		setTabToDisplayOn(CreativeTabs.tabFood);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			itemstack.stackSize--;
			if (!world.isRemote && potionId > 0 && world.rand.nextFloat() < potionEffectProbability) {
				entityplayer.addPotionEffect(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
			}

			if (poisonChance > 0 && ConfigHelper.poisonOn == true) {
				Random rand = new Random();
				if(rand.nextFloat() < poisonChance) {
					PoisonController.startPoison();
				}
			}

			if (foodHeal > 0 && satHeal > 0) {
				entityplayer.getFoodStats().addStats(foodHeal, satHeal);
			}

			if (itemstack.stackSize <= 0) {
				return new ItemStack(returnItem);
			} else {
				entityplayer.inventory.addItemStackToInventory(new ItemStack(returnItem));
				return itemstack;
			}
		}
		return itemstack;
	}

	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if (canDrink() == true || alwaysDrinkable == true) {
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
		}
		return itemstack;
	}

	/**
	 * Sets a potion effect when the drink is drunk.
	 * @param i Potion ID
	 * @param j Duration
	 * @param k Amplifier
	 * @param f Probability
	 * @return
	 */
	public Drink setPotionEffect(int i, int j, int k, float f) {
		potionId = i;
		potionDuration = j;
		potionAmplifier = k;
		potionEffectProbability = f;
		return this;
	}

	public boolean hasEffect(ItemStack itemstack) {
		if (hasEffect == true) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets a poisoning chance when drunk.
	 * @param chance Chance. 0.6f = 60% approximately.
	 * @return
	 */
	public Drink setPoisoningChance(float chance) {
		poisonChance = chance;
		return this;
	}

	/**
	 * Makes the item shiny like Golden Apple.
	 * @return this
	 */
	public Item setHasEffect() {
		hasEffect = true;
		return this;
	}

	/**
	 * Makes the item shiny like Golden Apple. This one is for ContentLoader
	 * cause its a boolean.
	 * @return this
	 */

	public Drink setEffect(boolean b) {
		if (b == true) {
			hasEffect = true;
		} else if (b == false) {
			// Do nothing!
		}
		return this;
	}

	/**
	 * Allows the drink to heal the food bar.
	 * @param level amount level.
	 * @param saturation amount satuation.
	 * @return
	 */
	public Drink healFood(int level, float saturation) {
		foodHeal = level;
		satHeal = saturation;
		return this;
	}

	/**
	 * Can the person drink.
	 * @return
	 */
	public static boolean canDrink() {
		if (ThirstUtils.getStats().level < 20) {
			return true;
		} else if (FMLClientHandler.instance().getClient().thePlayer.capabilities.isCreativeMode == true) {
			return true;
		}
		return false;
	}

	public Item setReturn(Item item) {
		returnItem = item;
		return this;
	}

	/**
	 * Sets the items 256*256 sprite sheet. Used for forge.
	 * @param tex
	 * @return
	 */
	public Item setTexFile(String tex) {
		texture = tex;
		return this;
	}

	@Override
	public String getTextureFile() {
		return texture;
	}
}