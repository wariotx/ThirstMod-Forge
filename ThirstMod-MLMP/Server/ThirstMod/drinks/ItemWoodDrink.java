package net.minecraft.src.ThirstMod.drinks;

import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.core.ThirstStats;
import net.minecraft.src.ThirstMod.core.ThirstUtils;
import net.minecraft.src.ThirstMod.other.PlayerThirst;

public class ItemWoodDrink extends Item
{
	private final int healAmount;
	private final float saturationThirstModifier;
	private boolean alwaysDrinkable;
	private Random rand = new Random();

	public ItemWoodDrink(int id, int thirstReplenish, float saturationModifier)
	{
		super(id);
		healAmount = thirstReplenish;
		saturationThirstModifier = saturationModifier;
		setMaxStackSize(mod_ThirstMod.maxStackSize);
	}

	public ItemWoodDrink(int i, int j)
	{
		this(i, j, 0.2F);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		itemstack.stackSize--;
		getStats().addThirstStatsFrom(this);
		if (itemstack.stackSize <= 0)
		{
			return new ItemStack(mod_ThirstMod.woodBottle);
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
		if (canDrink(alwaysDrinkable))
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

	public ItemWoodDrink setPoisoningChance(float chance)
	{
		if (rand.nextFloat() < chance && mod_ThirstMod.isThirstPoisoningOn == true)
		{
			PlayerThirst.getStats().isThirstPoisoned = true;
		}
		return this;
	}

	public ThirstStats getStats()
	{
		return PlayerThirst.getStats();
	}

	public static boolean canDrink(boolean flag)
	{
		return ((flag || PlayerThirst.getStats().needWater()) && !PlayerThirst.getStats().isThirstPoisoned);
	}
}
