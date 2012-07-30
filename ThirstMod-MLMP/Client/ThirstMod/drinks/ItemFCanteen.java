package net.minecraft.src.ThirstMod.drinks;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.core.ThirstUtils;

public class ItemFCanteen extends Item
{
	private String[] FCanteenNames =
	{ "fCanteen0", "fCanteen1", "fCanteen2", "fCanteen3", "fCanteen4", "fCanteen5" };

	public ItemFCanteen(int i)
	{
		super(i);
		setMaxStackSize(1);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public String getItemNameIS(ItemStack itemstack)
	{
		return FCanteenNames[itemstack.getItemDamage()];
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (itemstack.getItemDamage() > 0)
		{
			ThirstUtils.getStats().addStats(2, 2.8F);
			if (itemstack.getItemDamage() == 1)
			{
				return new ItemStack(mod_ThirstMod.canteen, 1);
			} else
			{
				return new ItemStack(this, 1, itemstack.getItemDamage() - 1);
			}
		}
		return itemstack;
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
				if (itemstack.getItemDamage() < 5)
				{
					return new ItemStack(mod_ThirstMod.canteen, 1, 5);
				}
			} else if (itemstack.getItemDamage() > 0 && ThirstUtils.getStats().needWater() == true)
			{
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
		}
		return itemstack;

	}
}