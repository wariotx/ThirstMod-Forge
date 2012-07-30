package net.minecraft.src.ThirstMod.core;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.other.*;
import net.minecraft.src.ThirstMod.containers.JMRecipes;
import net.minecraft.src.ThirstMod.containers.WaterCollectorRecipes;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ThirstUtils 
{
	private static ThirstStats thirstStats = new ThirstStats();
	private static List mcblocks = new ArrayList();
	private static List mcitems = new ArrayList();
	
	public static ThirstStats getStats()
	{
		return thirstStats;
	}
	
	public static String getVersion()
	{
		return "0.8";
	}
	
	public static void addBlock(Block block)
	{
		addSubtypeBlock(block, 0);
	}
	
	public static void addSubtypeBlock(Block block, int number)
	{
		mcblocks.add(new ItemStack(block, 1, number));
	}
	
	public static void addSubtypeItem(Item item, int number)
	{
		mcitems.add(new ItemStack(item, 1, number));
		if(number == 0)
		{	
			throw new IllegalArgumentException("A mod tried to add an item using the suptype ID: 0");
		}
	}
	
	public static void addJuiceRecipe(int i, ItemStack itemstack)
    {
        JMRecipes.solidifying().addSolidifying(i, itemstack);
    }
	
	public static void addJuiceRecipe(int i, int j, ItemStack item)
	{
		JMRecipes.solidifying().addSolidifying(i, j, item);
	}
	
	public static void addRCRecipe(int itemID, ItemStack itemstack)
	{
		WaterCollectorRecipes.fill().addRecipe(itemID, itemstack);
	}
	
	public static void setThirstLevel(int level)
	{
		thirstStats.setThirstLevel(level);
	}
	
	public static void addThirstStats(int replenish, float saturation)
	{
		thirstStats.addStats(replenish, saturation);
	}
	
	public static List getItem()
	{
		return mcitems;
	}
	
	public static List getBlock()
	{
		return mcblocks;
	}

	
	public static void addExhaustion(float f)
	{
		PlayerThirst.getStats().addExhaustion(f);
	}
	
	public static int getMovementStat(EntityPlayer entityplayer)
	{
		double s = entityplayer.posX;
		double s1 = entityplayer.posY;
		double s2 = entityplayer.posZ;
		double d = s - entityplayer.prevPosX;
		double d1 = s1 - entityplayer.prevPosY;
		double d2 = s2 - entityplayer.prevPosZ;
		return Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
	}
	
	public static String getCurrentBiome(EntityPlayer entityplayer)
	{
    	return entityplayer.worldObj.getWorldChunkManager().getBiomeGenAt((int)entityplayer.posX, (int)entityplayer.posZ).biomeName;
    }
	
	public static void makeDefault()
	{
		thirstStats.thirstTickTimer = 0;
		thirstStats.thirstLevel = 20;
		thirstStats.prevthirstLevel = 20;
		thirstStats.thirstSaturationLevel1 = 5F;
		thirstStats.isThirstPoisoned = false;
		thirstStats.thirstDuration = 360;
		thirstStats.thirstHang = 0;
	}
	
	public static void deleteFiles(String directory, String extension) 
	{
		ExtensionFilter filter = new ExtensionFilter(extension);
		File dir = new File(directory);

		String[] list = dir.list(filter);
		File file;
		if (list.length == 0)
		{
			return;
		}

		for (int i = 0; i < list.length; i++) 
		{
			file = new File(directory + list[i]);
			boolean isdeleted = file.delete();
		}
	}
	
	public static boolean classExists(String className)
	{
		try
		{
			boolean name = Class.forName(className) != null;
			if(name == true)
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("Class not found: " + className);
			return false;
		}
		return false;
	}
	
	public static String getDir()
	{
		try
        {
            String s = (net.minecraft.src.ModLoader.class).getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            s = s.substring(0, s.lastIndexOf('/'));
            return s;
        }
        catch (URISyntaxException urisyntaxexception)
        {
            return "";
        }
	}
	
	public static void print(Object obj)
	{
		System.out.println(obj);
	}
}
