package net.minecraft.src.ThirstMod.drinks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.core.ThirstStats;
import net.minecraft.src.ThirstMod.core.ThirstUtils;
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
	
	private float poisonChance;
	
	private int foodHeal;
	private float satHeal;

	public Drink(int id, int thirstReplenish, float saturationModifier, boolean alwaysDrinkable)
	{
		super(id);
		healAmount = thirstReplenish;
		saturationThirstModifier = saturationModifier;
		setMaxStackSize(mod_ThirstMod.maxStackSize);
		if (alwaysDrinkable == true)
		{
			this.alwaysDrinkable = true;
		}
	}

	public Drink(int i, int j, boolean flag)
	{
		this(i, j, 0.2F, flag);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		itemstack.stackSize--;
		getStats().addStats(healAmount, saturationThirstModifier);
		if (!world.isRemote && potionId > 0 && world.rand.nextFloat() < potionEffectProbability)
		{
			entityplayer.addPotionEffect(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
		}
		
		if(poisonChance > 0)
		{
			ThirstUtils.getStats().isThirstPoisoned = true;
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

	public Drink setPoisoningChance(float chance)
	{
		poisonChance = chance;
		return this;
	}

	public ThirstStats getStats()
	{
		return ThirstUtils.getStats();
	}

	public Item setHasEffect()
	{
		hasEffect = true;
		return this;
	}
	
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
	
	public Drink healFood(int level, float saturation)
	{
		foodHeal = level;
		satHeal = saturation;
		return this;
	}

	public static boolean canDrink()
	{
		return true;
	}
}