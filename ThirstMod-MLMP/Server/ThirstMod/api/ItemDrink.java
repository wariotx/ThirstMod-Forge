//Make your drink extend this class, to make it a drink.

package net.minecraft.src.ThirstMod.api;

import java.io.File;
import java.io.FileInputStream;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.core.ThirstStats;

public abstract class ItemDrink extends Item
{
	private int thirstLevel;
	private float thirstSat;
	
	public ItemDrink(int id)
	{
		super(id);
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}
	
	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		ThirstStats.addStats(getHealAmount(), getSaturationModifier());
		return super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
	}
	
	//Override this method and make it return the amount your drink will heal. In half values, 10 droplets = 20 in this method.
	public int getHealAmount()
	{
		return 0;
	}

	//Override this method to heal the saturation. Same as FoodStats.class "saturationLevel." Study it for further details.
	public float getSaturationModifier()
	{
		return 0;
	}
	
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.drink;
	}
}
