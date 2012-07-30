package net.minecraft.src.ThirstMod.items;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_ThirstMod;
import net.minecraft.src.ThirstMod.*;
import java.util.Random;

public class ItemRainMaker extends Item
{	
	public ItemRainMaker(int i)
	{
		super(i);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}
	
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		Minecraft minecraft = ModLoader.getMinecraftInstance();
		if(!mod_ThirstMod.getWorldInfo().isRaining() && !mod_ThirstMod.getWorldInfo().isThundering() && minecraft.thePlayer.capabilities.disableDamage == true)
		{
			{
				mod_ThirstMod.getWorldInfo().setThunderTime(0);
                mod_ThirstMod.getWorldInfo().setThundering(false);
                mod_ThirstMod.getWorldInfo().setRainTime((new Random()).nextInt(12000) + 12000);
                mod_ThirstMod.getWorldInfo().setRaining(true);
			}
		}
		entityplayer.swingItem();
		return itemstack;
    }

	public String getTextureFile() 
	{
		return "/ThirstMod/ThirstTextures.png";
	}
}
