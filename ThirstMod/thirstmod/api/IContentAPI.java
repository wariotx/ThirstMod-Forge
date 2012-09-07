package net.minecraft.src.thirstmod.api;

public interface IContentAPI {
	
	/**
	 * Called when a file is read from /mods/ThirstMod/Content/Main/
	 * @param fileName The name of the file.
	 * @param dataRead The BufferedReader line.split for reading all the data in the file. Refer to ContentLoader.read
	 * @param mainDirectory The Main directory where all the drinks are located.
	 * @param texFile The name of the texture file used for the drinks.
	 */
	public void onMainContentLoad(String fileName, String[] dataRead, String mainDirectory, String texFile);
	
	/**
	 * Called when a drink from /mods/ThirstMod/Content/Files/Main Directory(See above)
	 * @param fileName The name of the file.
	 * @param dataRead The BufferedReader line.split for reading all the data in the file. Refer to ContentDrink.read
	 */
	public void onDrinkContentLoad(String fileName, String[] dataRead);
}
