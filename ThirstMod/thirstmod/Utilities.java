package net.minecraft.src.thirstmod;

import java.io.*;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.content.*;

public class Utilities {
	private static ThirstStats thirstStats = new ThirstStats();
	
	public static int getMovementStat(EntityPlayer entityplayer) {
		double s = entityplayer.posX;
		double s1 = entityplayer.posY;
		double s2 = entityplayer.posZ;
		double d = s - entityplayer.prevPosX;
		double d1 = s1 - entityplayer.prevPosY;
		double d2 = s2 - entityplayer.prevPosZ;
		return Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
	}
	
	public static String getCurrentBiome(EntityPlayer entityplayer) {
    	return entityplayer.worldObj.getWorldChunkManager().getBiomeGenAt((int)entityplayer.posX, (int)entityplayer.posZ).biomeName;
    }
	
	public static boolean classExists(String class1) {
		try {
			boolean class2 = Class.forName(class1) != null;
			if(class2 = true) {
				return true;
			}
		} catch(ClassNotFoundException e) {
			print("Class not found: " + class1);
			return false;
		}
		return false;
	}
	
	public static void setDefault() {
		getStats().level = 20;
		getStats().exhaustion = 0.0f;
		getStats().saturation = 5f;
		getStats().healTimer = 0;
		getStats().poisoned = false;
		getStats().shiftGain = 0;
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
	
	public void registerContent(ContentMain conMain) {
		conMain.prepare();
	}
	
	public static void print(Object obj) {
		System.out.println(obj);
	}
	
	public static ThirstStats getStats() {
		return thirstStats;
	}
}
