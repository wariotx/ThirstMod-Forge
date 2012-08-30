package net.minecraft.src.thirstmod;

import java.io.*;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.*;
import java.util.*;
import net.minecraft.client.Minecraft;

public class ContentLoader
{
	private static List addedfiles = new ArrayList();
	private String directory;
	private String textureFile;
	
	public ContentLoader() {
		loadMainFiles();
	}
	
	/**
	 * Loads the main files to determine the custom drinks texture and directories.
	 */
	public void loadMainFiles()
	{
		File apiDir = new File(Minecraft.getMinecraftDir(), "/mods/ThirstMod/Content/Main/");
		
		boolean deleted = false;
		
		if (!apiDir.exists())
		{
			apiDir.mkdir();
			return;
		}
		List<File> filesList = new ArrayList<File>();
		for (File file : apiDir.listFiles())
		{
			if (file.isDirectory())
			{
				continue;
			}
			filesList.add(file);
		}
		for (File file : filesList)
		{
			File[] files = apiDir.listFiles();
			if (files.length == 0)
			{
				System.out.println("No Files found");
			} else
			{
				for (int i = 0; i < files.length; i++)
				{
					if (deleted == false)
					{
						//Deletes the .ini and .DS_Store files that can cause conflicts in the loading process.
						ThirstUtils.deleteFiles(Minecraft.getMinecraftDir() + "/mods/ThirstMod/Content/Main/", ".DS_Store");
						ThirstUtils.deleteFiles(Minecraft.getMinecraftDir() + "/mods/ThirstMod/Content/Main/", ".ini");
						deleted = true;
					}
					if (!addedfiles.contains(files[i].getName()) && deleted == true)
					{
						try
						{
							init(new BufferedReader(new FileReader(files[i])));
							addedfiles.add(files[i].getName());
							new ContentDrink(directory, textureFile);
						} catch (Exception e)
						{
							System.out.println("File not loaded:" + files[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
		//Adds a custom biome poisoning file. For Custom Biomes from other mods.
		File customBiomePoisoning = new File(Minecraft.getMinecraftDir() + "/mods/ThirstMod/Custom Biome Posioning.txt");
		if (!customBiomePoisoning.exists())
		{
			try
			{
				customBiomePoisoning.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else if (customBiomePoisoning.length() > 0)
		{
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(customBiomePoisoning));
				String line = br.readLine();
				String[] split = line.split(":");

				PoisonController.getBiomesList().put(split[0], Float.parseFloat(split[1]));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void init(BufferedReader br)
	{
		while (true) 
		{
			String line = null;
			try 
			{
				line = br.readLine();
			} 
			catch (Exception e) 
			{
				break;
			}
			if (line == null) 
			{
				break;
			}
			if (line.startsWith("//"))
				continue;
			String[] colon = line.split(":");
			if (colon.length < 2)
				continue;
			read(colon);
		}
	}
	
	/**
	 * Reads the text file.
	 * @param colon The character that all the information is written after. In this case ':'
	 */
	public void read(String[] colon)
	{
		if(colon[0].equals("Directory")) {
			directory = Minecraft.getMinecraftDir().toString() + "/mods/ThirstMod/Content/Files/" + colon[1];
		}
		if(colon[0].equals("Texture"))
		{
			textureFile = "/Content/Textures/" + colon[1];
		}
	}
}
