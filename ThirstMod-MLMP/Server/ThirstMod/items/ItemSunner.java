package net.minecraft.src.ThirstMod.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_ThirstMod;
import net.minecraft.src.ThirstMod.*;

public class ItemSunner extends Item
{	
	public ItemSunner(int i)
	{
		super(i);
		setMaxStackSize(0);
	}
	
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if(mod_ThirstMod.getWorldInfo(entityplayer).isRaining() && entityplayer.capabilities.disableDamage == true)
        {
        	mod_ThirstMod.getWorldInfo(entityplayer).setRaining(false);
        	mod_ThirstMod.getWorldInfo(entityplayer).setThundering(false);
        	mod_ThirstMod.getWorldInfo(entityplayer).setRainTime(0);
        	mod_ThirstMod.getWorldInfo(entityplayer).setThunderTime(0);
        }
        entityplayer.swingItem();
        return itemstack;
    }
}