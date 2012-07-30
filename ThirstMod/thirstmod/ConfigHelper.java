package net.minecraft.src.thirstmod;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.*;

public class ConfigHelper {
	private static Configuration config = new Configuration(new File(Minecraft.getMinecraftDir() + "/mods/ThirstMod/" + "Config.txt")); 
	
	public static boolean poisonOn = setupConfig();
	public static boolean peacefulOn;
	public static boolean meterOnLeft;
	public static boolean oldTextures;
	public static int thirstRate;
	public static int maxStackSize;
	
	public static boolean wantMilk;
	public static boolean wantCMilk;
	public static boolean wantFBucket;
	
	public static int milkId;
	public static int cMilkId;
	public static int fBucketId;
	public static int freshWaterId;
	public static int jmId;
	public static int rcId;
	public static int canteenId;
	public static int filterId;
	public static int dFilterId;
	
	private static boolean setupConfig() {
		config.load();
		
		poisonOn = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Poisoning On", "General", true).value);
		peacefulOn = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Peaceful On", "General", false).value);
		meterOnLeft = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Meter on leftside", "General", true).value);
		oldTextures = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Old Meter Textures", "General", true).value);
		thirstRate = Integer.parseInt(config.getOrCreateIntProperty("Exhaustion Rate", "General", 10).value);
		maxStackSize = Integer.parseInt(config.getOrCreateIntProperty("Max Stack Size", "General", 10).value);
		
		wantMilk = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Milk", "Drinks", true).value);
		wantCMilk = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Chocolate Milk", "Drinks", true).value);
		wantFBucket = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Fresh Bucket", "Drinks", true).value);
		
		milkId = Integer.parseInt(config.getOrCreateIntProperty("Milk", "Item", 600).value);
		cMilkId = Integer.parseInt(config.getOrCreateIntProperty("Chocolate Milk", "Item", 601).value);
		fBucketId = Integer.parseInt(config.getOrCreateIntProperty("Fresh Water Bucket", "Item", 602).value);
		freshWaterId = Integer.parseInt(config.getOrCreateIntProperty("Fresh Water", "Item", 603).value);
		canteenId = Integer.parseInt(config.getOrCreateIntProperty("Canteen", "Item", 604).value);
		filterId = Integer.parseInt(config.getOrCreateIntProperty("Filter", "Item", 605).value);
		dFilterId = Integer.parseInt(config.getOrCreateIntProperty("Dirty Filter", "Item", 606).value);
		
		jmId = Integer.parseInt(config.getOrCreateBlockIdProperty("Drinks Brewer", 3505).value);
		rcId = Integer.parseInt(config.getOrCreateBlockIdProperty("Rain Collector", 3506).value);
		
		config.save();
		return poisonOn;
	}
}
