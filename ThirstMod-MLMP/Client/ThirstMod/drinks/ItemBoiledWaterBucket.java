package net.minecraft.src.ThirstMod.drinks;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.core.ThirstUtils;

public class ItemBoiledWaterBucket extends Item
{
	private int isFull;

	public ItemBoiledWaterBucket(int i)
	{
		super(i);
		maxStackSize = 1;
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		itemstack.stackSize--;
		ThirstUtils.getStats().addStats(10, 6.4F);
		if (itemstack.stackSize <= 0)
		{
			return new ItemStack(Item.bucketEmpty);
		} else
		{
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
		if (ThirstUtils.getStats().needWater() == true)
		{
			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}
		return itemstack;
	}
}
