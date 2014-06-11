/**
 * ThirstMods Drink class. Similar to ItemFood.
 */
package tarun1998.thirstmod;

import java.util.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import tarun1998.thirstmod.utils.*;

public class Drink extends Item {
	private int thirstReplenish;
	private float saturationReplenish;
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

	public Drink(int id, int replenish, float saturation, boolean alwaysDrinkable) {
		super(id);
		if (alwaysDrinkable == true) {
			this.alwaysDrinkable = true;
		}
		setTabToDisplayOn(CreativeTabs.tabFood);
		this.thirstReplenish = replenish;
		this.saturationReplenish = saturation;
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if(!world.isRemote) {
			ThirstUtils.getUtilsFor(entityplayer.username).getStats().addStats(thirstReplenish, saturationReplenish);
			if (poisonChance > 0 && ConfigHelper.poisonOn == true) {
				Random rand = new Random();
				if(rand.nextFloat() < poisonChance) {
					ThirstUtils.getUtilsFor(entityplayer.username).getStats().getPoison().startPoison();
				}
			}
		
			if (foodHeal > 0 && satHeal > 0) {
				entityplayer.getFoodStats().addStats(foodHeal, satHeal);
			}
			
			if(potionId > 0 && world.rand.nextFloat() < potionEffectProbability) {
				entityplayer.addPotionEffect(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
			}
		}
		if(itemstack.stackSize > 1 && !world.isRemote) {
			entityplayer.inventory.addItemStackToInventory(new ItemStack(returnItem));
			return new ItemStack(this, itemstack.stackSize - 1);
		} else {
			return new ItemStack(returnItem);
		}
	}

	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (canDrink() == true || alwaysDrinkable == true || entityplayer.capabilities.isCreativeMode == true) {
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
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
	public boolean canDrink() {
		if (ThirstUtils.getUtilsFor(ThirstUtils.getPlayerName()).getStats().level < 20) {
			return true;
		} 
		return false;
	}

	/**
	 * Sets the item that is returned.
	 * @param item Item that is returned after the drink is drunk.
	 * @return this.
	 */
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
	
	/**
	 * Gets the return item for this drink.
	 * @return the item that will be given after this drink is drunk.
	 */
	public Item getReturn() {
		return returnItem;
	}
}