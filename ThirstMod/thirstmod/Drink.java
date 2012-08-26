/**
 * ThirstMods Drink class. Similar to ItemFood.
 */
package net.minecraft.src.thirstmod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.*;

public class Drink extends Item
{
	private final int healAmount;
	private final float saturationThirstModifier;
	private boolean alwaysDrinkable;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;
	private boolean hasEffect = false;
	private Random rand = new Random();
	private String texture;
	
	private float poisonChance;
	
	private int foodHeal;
	private float satHeal;

	public Drink(int id, int thirstReplenish, float saturationModifier, boolean alwaysDrinkable)
	{
		super(id);
		healAmount = thirstReplenish;
		saturationThirstModifier = saturationModifier;
		if (alwaysDrinkable == true)
		{
			this.alwaysDrinkable = true;
		}
		setTabToDisplayOn(CreativeTabs.tabFood);
	}

	public Drink(int i, int j, boolean flag)
	{
		this(i, j, 0.2F, flag);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		itemstack.stackSize--;
		ThirstUtils.getStats().addStats(healAmount, saturationThirstModifier);
		if (!world.isRemote && potionId > 0 && world.rand.nextFloat() < potionEffectProbability)
		{
			entityplayer.addPotionEffect(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
		}
		
		if(poisonChance > 0)
		{
			PoisonController.startPoison();
		}
		
		if(foodHeal > 0 && satHeal > 0)
		{
			entityplayer.getFoodStats().addStats(foodHeal, satHeal);
		}
		
		if (itemstack.stackSize <= 0)
		{
			return new ItemStack(Item.glassBottle);
		} else
		{
			entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
			return itemstack;
		}
	}

	public int getMaxItemUseDuration(ItemStack itemstack)
	{
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		return itemstack;
	}

	public int getHealAmount()
	{
		return healAmount;
	}

	public float getThirstSaturationModifier()
	{
		return saturationThirstModifier;
	}

	/** 
	 * Sets a potion effect when the drink is drunk.
	 * @param i Potion ID
	 * @param j Duration
	 * @param k Amplifier
	 * @param f Probability
	 * @return
	 */
	public Drink setPotionEffect(int i, int j, int k, float f)
	{
		potionId = i;
		potionDuration = j;
		potionAmplifier = k;
		potionEffectProbability = f;
		return this;
	}

	public boolean hasEffect(ItemStack itemstack)
	{
		if (hasEffect == true)
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * Sets a poisoning chance when drunk.
	 * @param chance Chance. 0.6f = 60% approximately.
	 * @return
	 */
	public Drink setPoisoningChance(float chance)
	{
		poisonChance = chance;
		return this;
	}

	/**
	 * Makes the item shiny like Golden Apple.
	 * @return this
	 */
	public Item setHasEffect()
	{
		hasEffect = true;
		return this;
	}
	
	/**
	 * Makes the item shiny like Golden Apple. This one is for ContentDrink cause its a boolean.
	 * @return this
	 */

	public Drink setEffect(boolean b)
	{
		if(b == true)
		{
			hasEffect = true;
		}
		else if(b == false)
		{
			//Do nothing!
		}
		return this;
	}
	
	/**
	 * Allows the drink to heal the food bar.
	 * @param level amount level.
	 * @param saturation amount satuation.
	 * @return
	 */
	public Drink healFood(int level, float saturation)
	{
		foodHeal = level;
		satHeal = saturation;
		return this;
	}

	/**
	 * Can the person drink. Set to true for debugging.
	 * @return
	 */
	public static boolean canDrink()
	{	
		/*
		if(ThirstUtils.getStats().level < 20) {
			return true;
		}
		return false.
		*/
		return true;
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