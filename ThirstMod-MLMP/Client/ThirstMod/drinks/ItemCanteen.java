package net.minecraft.src.ThirstMod.drinks;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.core.ThirstUtils;

public class ItemCanteen extends Item
{
	private String[] canteenNames =
	{ "Canteen0", "Canteen1", "Canteen2", "Canteen3", "Canteen4", "Canteen5", "Canteen6", "Canteen7", "Canteen8", "Canteen9", "Canteen10", };

	public ItemCanteen(int i)
	{
		super(i);
		setMaxStackSize(1);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		return canteenNames[itemstack.getItemDamage()];
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (itemstack.getItemDamage() > 0)
		{
			ThirstUtils.getStats().addStats(2, 1.2F);
			if (itemstack.getItemDamage() <= 5 && !world.isRemote && world.rand.nextFloat() < 0.2 && mod_ThirstMod.isThirstPoisoningOn)
			{
				ThirstUtils.getStats().isThirstPoisoned = true;
			}
			return new ItemStack(this, 1, getDecrementedDamage(itemstack.getItemDamage()));
		} else
		{
			return itemstack;
		}
	}

	public int getDecrementedDamage(int i)
	{
		if (i == 6)
		{
			return 0;
		}
		return i - 1;
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
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(world, entityplayer, true);
		if (movingobjectposition == null)
		{
			if (itemstack.getItemDamage() > 0 && ThirstUtils.getStats().needWater() == true)
			{
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
			return itemstack;
		}
		if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
		{
			int i = movingobjectposition.blockX;
			int j = movingobjectposition.blockY;
			int k = movingobjectposition.blockZ;
			if (world.getBlockMaterial(i, j, k) == Material.water)
			{
				if (itemstack.getItemDamage() < 5 || (itemstack.getItemDamage() >= 6 && itemstack.getItemDamage() < 10))
				{
					return new ItemStack(this, 1, 5);
				}
			} else if (itemstack.getItemDamage() > 0 && ThirstUtils.getStats().needWater() == true)
			{
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
		}
		return itemstack;

	}
}
