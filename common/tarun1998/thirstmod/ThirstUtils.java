package tarun1998.thirstmod;

import java.io.*;
import java.net.*;
import tarun1998.thirstmod.blocks.JMRecipes;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.src.*;

public class ThirstUtils {
	public static final String NAME = "Thirst mod";
	public static final String ID = "ThirstMod";
	public static final String VERSION = "1.1.0";

	private static String currentDir;

	private static PlayerStatistics stats = new PlayerStatistics();

	/**
	 * Gets the mod version.
	 * @return the mod version.
	 */
	public static String getModVersion() {
		return VERSION;
	}

	/**
	 * Gets the PlayerStatistics.class instance.
	 * @return the PlayerStatistics.class instance.
	 */
	public static PlayerStatistics getStats() {
		return stats;
	}

	/**
	 * Gets the players current speed in blocks per tick.
	 * @param entityplayer instance.
	 * @return players current speed.
	 */
	public static int getMovementStat(EntityPlayer entityplayer) {
		double s = entityplayer.posX;
		double s1 = entityplayer.posY;
		double s2 = entityplayer.posZ;
		double d = s - entityplayer.prevPosX;
		double d1 = s1 - entityplayer.prevPosY;
		double d2 = s2 - entityplayer.prevPosZ;
		return Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
	}

	/**
	 * Gets the current biome the player is in.
	 * @param entityplayer
	 * @return the current biome of the player.
	 */
	public static String getCurrentBiome(EntityPlayer entityplayer) {
		return entityplayer.worldObj.getWorldChunkManager().getBiomeGenAt((int) entityplayer.posX, (int) entityplayer.posZ).biomeName;
	}

	/**
	 * Writes all the data in PlayerStatistics to the NBT.
	 * @param nbt Player NBT.
	 */
	public static void writeNbt(NBTTagCompound nbt) {
		nbt.setInteger("tmLevel", getStats().level);
		nbt.setFloat("tmExhaustion", getStats().exhaustion);
		nbt.setFloat("tmSaturation", getStats().saturation);
		nbt.setInteger("tmTimer", getStats().healhurtTimer);
		nbt.setInteger("tmTimer2", getStats().drinkTimer);
	}

	/**
	 * Reads the data from the nbt and applies it to the data in
	 * PlayerStatistics.class.
	 * @param nbt Player NBT
	 */
	public static void readNbt(NBTTagCompound nbt) {
		if (nbt.hasKey("tmLevel")) {
			getStats().level = nbt.getInteger("tmLevel");
			getStats().exhaustion = nbt.getFloat("tmExhaustion");
			getStats().saturation = nbt.getFloat("tmSaturation");
			getStats().healhurtTimer = nbt.getInteger("tmTimer");
			getStats().drinkTimer = nbt.getInteger("tmTimer2");
		} else {
			setDefaults();
		}
	}

	/**
	 * Sets all the data in PlayerStatistics to their original values.
	 */
	public static void setDefaults() {
		getStats().level = 20;
		getStats().exhaustion = 0f;
		getStats().saturation = 5f;
		getStats().healhurtTimer = 0;
		getStats().drinkTimer = 0;
	}

	public static void setModUnloaded() {
		ThirstMod.INSTANCE.loadedMod = false;
	}

	/**
	 * Gets the Thirst Level from PlayerStatistics.class
	 * @return the level.
	 */
	public static int getLevel() {
		return getStats().level;
	}

	/**
	 * Gets the Saturation Level from PlayerStatistics.class
	 * @return the saturation value.
	 */
	public static float getSaturation() {
		return getStats().saturation;
	}

	/**
	 * Gets the Exhaustion Level from PlayerStatistics.class
	 * @return the exhaustion level.
	 */
	public static float getExhaustion() {
		return getStats().exhaustion;
	}

	/**
	 * Adds exhaustion to the player.
	 * @param f amount of exhaustion to add.
	 */
	public void addExhaustion(float f) {
		getStats().addExhaustion(f);
	}

	/**
	 * Deletes files beginning with a specific extension. Used to avoid
	 * conflicts in the ContentLoader.class
	 * @param directory Directory of the file.
	 * @param extension Files beginning with this extension to delete.
	 */
	public static void deleteFiles(String directory, String extension) {
		ExtensionFilter filter = new ExtensionFilter(extension);
		File dir = new File(directory);

		String[] list = dir.list(filter);
		File file;
		if (list.length == 0) {
			return;
		}

		for (int i = 0; i < list.length; i++) {
			file = new File(directory + list[i]);
			boolean isdeleted = file.delete();
		}
	}

	/**
	 * Adds a recipe to the Drinks Brewer.
	 * @param id Item that is placed in the top Drinks Brewer Slot.
	 * @param item The Item that is returned after the item (int id) is brewed.
	 */
	public static void addJMRecipe(int id, ItemStack item) {
		JMRecipes.solidifying().addSolidifying(id, item);
	}

	/**
	 * Adds a recipe to the Drinks Brewer.
	 * @param id Item that is placed in the top Drinks Brewer Slot.
	 * @param j Metadata for (int i) if needed.
	 * @param item The Item that is returned after the item (int id, int
	 * metadata) is brewed.
	 */
	public static void addJMRecipe(int id, int metadata, ItemStack item) {
		JMRecipes.solidifying().addSolidifying(id, metadata, item);
	}

	public static void setupCurrentDir(FMLPreInitializationEvent event) {
		File one = event.getSuggestedConfigurationFile();
		File two = one.getParentFile();
		File three = two.getParentFile();
		currentDir = three.getAbsolutePath();
	}

	public static String getDir() {
		return currentDir;
	}
}

/**
 * Class that deletes files beginning with a specific extension.
 */
class ExtensionFilter implements FilenameFilter {
	private String extension;

	public ExtensionFilter(String extension) {
		this.extension = extension;
	}

	public boolean accept(File dir, String name) {
		return (name.endsWith(extension));
	}
}