package net.minecraft.src.ThirstMod.items;

import net.minecraft.client.Minecraft;
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
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		if(mod_ThirstMod.getWorldInfo().isRaining() && minecraft.thePlayer.capabilities.disableDamage == true)
        {
        	mod_ThirstMod.getWorldInfo().setRaining(false);
        	mod_ThirstMod.getWorldInfo().setThundering(false);
        	mod_ThirstMod.getWorldInfo().setRainTime(0);
        	mod_ThirstMod.getWorldInfo().setThunderTime(0);
        }
        entityplayer.swingItem();
        return itemstack;
    }
}