package net.minecraft.src.ThirstMod.drinks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.core.ThirstStats;
import net.minecraft.src.ThirstMod.core.ThirstUtils;
import net.minecraft.src.*;

public class ItemWoodDrink extends Item
{
	private final int healAmount;
	private final float saturationThirstModifier;
	private boolean alwaysItemWoodDrinkable;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;
	private boolean hasEffect = false;
	private Random rand = new Random();
	
	private float poisonChance;

	public ItemWoodDrink(int id, int thirstReplenish, float saturationModifier)
	{
		super(id);
		healAmount = thirstReplenish;
		saturationThirstModifier = saturationModifier;
		setMaxStackSize(mod_ThirstMod.maxStackSize);
		if (alwaysItemWoodDrinkable == true)
		{
			this.alwaysItemWoodDrinkable = true;
		}
	}

	public ItemWoodDrink(int i, int j)
	{
		this(i, j, 0.2F);
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
			if(rand.nextFloat() < poisonChance && mod_ThirstMod.isThirstPoisoningOn == true)
			{
				ThirstUtils.getStats().isThirstPoisoned = true;
			}
		}
		
		if (itemstack.stackSize <= 0)
		{
			return new ItemStack(mod_ThirstMod.woodBottle);
		} else
		{
			entityplayer.inventory.addItemStackToInventory(new ItemStack(mod_ThirstMod.woodBottle));
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
		if (canItemWoodDrink(alwaysItemWoodDrinkable))
		{
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}
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

	public ItemWoodDrink setPotionEffect(int i, int j, int k, float f)
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

	public ItemWoodDrink setPoisoningChance(float chance)
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
	
	public ItemWoodDrink setEffect(boolean b)
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

	public static boolean canItemWoodDrink(boolean flag)
	{
		if (ModLoader.getMinecraftInstance().thePlayer.capabilities.isCreativeMode)
		{
			return true;
		} else
		{
			return ((flag || ThirstUtils.getStats().needWater()) && !ThirstUtils.getStats().isThirstPoisoned);
		}
	}
}