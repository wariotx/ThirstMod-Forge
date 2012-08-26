/**
 * Loads the custom drinks.
 */
package net.minecraft.src.thirstmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;

public class ContentDrink extends ContentLoader {
	public static int id;
	public static int metadata;
	public static int texture;
	public static int replenish;
	public static int jmTop;
	public static float saturation;
	public static String name;
	public static String shortName;
	public static boolean alwaysDrinkable;
	public static boolean hasEffect;
	public static int maxStackSize;
	public static float poisonChance;
	public static boolean isShiny;
	public static int chance;
	public static int foodHeal;
	public static float satHeal;
	public static int texPos;
	
	public static int potionId;
	public static int potionDuration;
	public static int potionAmplifier;
	public static float potionEffectProbability;
	
	private static List addedDrinks = new ArrayList();
	
	public ContentDrink(String fileDir, String textureFile) {
		File apiDir = new File(fileDir);
		
		boolean deleted = false;
		
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
						ThirstUtils.deleteFiles(fileDir, ".DS_Store");
						ThirstUtils.deleteFiles(fileDir, ".ini");
						deleted = true;
					}
					if (!addedDrinks.contains(files[i].getName()) && deleted == true)
					{
						try
						{
							init(new BufferedReader(new FileReader(files[i])));
							Item drink = (((Drink) new Drink(id, replenish, saturation, alwaysDrinkable).setItemName(shortName)
									.setMaxStackSize(maxStackSize)).setEffect(isShiny)).setPoisoningChance(chance)
									.setPotionEffect(potionId, potionDuration, potionDuration, potionEffectProbability)
									.setTexFile(textureFile);
							MinecraftForgeClient.preloadTexture(textureFile);
							drink.setIconIndex(texPos);
							ModLoader.addName(drink, name);
							ThirstUtils.addJMRecipe(jmTop, metadata, new ItemStack(drink));
							addedDrinks.add(files[i].getName());
						} catch (Exception e)
						{
							System.out.println("Drink not loaded:" + files[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
		String loaded = addedDrinks.toString().replace("[", "").replace("]", "").replace(".txt", "");
		System.out.println("Thirst Mod loaded the following files: " + loaded);
	}
	
	@Override
	public void read(String[] colon) {
		super.read(colon);
		if (colon[0].equals("ID"))
		{
			id = Integer.parseInt(colon[1]);
		}
		if(colon[0].equals("TexturePos")) 
		{
			texPos = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("Replenish"))
		{
			replenish = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("Saturation"))
		{
			saturation = Float.parseFloat(colon[1]);
		}
		if (colon[0].equals("Name"))
		{
			name = colon[1];
			for (int i = 0; i < colon.length - 2; i++)
	        {
				name = (name + " " + colon[(i + 2)]);
	        }
		}
		if (colon[0].equals("ShortName"))
		{
			shortName = colon[1];
		}
		if (colon[0].equals("DBRecipe"))
		{
			jmTop = Integer.parseInt(colon[1]);
			metadata = Integer.parseInt(colon[2]);
		}
		if (colon[0].equals("AlwaysDrinkable"))
		{
			alwaysDrinkable = Boolean.parseBoolean(colon[1].toLowerCase());
		}
		if (colon[0].equals("MaxStackSize"))
		{
			maxStackSize = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("PoisonChance"))
		{
			poisonChance = Float.parseFloat(colon[1]);
		}
		if(colon[0].equals("Shiny"))
		{
			isShiny = Boolean.parseBoolean(colon[1].toLowerCase());
		}
		if(colon[0].equals("FoodHeal"))
		{
			foodHeal = Integer.parseInt(colon[1]);
			satHeal = Float.parseFloat(colon[2]);
		}
		if(colon[0].equals("PotionEffect"))
		{
			potionId = Integer.parseInt(colon[1]);
			potionDuration = Integer.parseInt(colon[2]);
			potionAmplifier = Integer.parseInt(colon[3]);
			potionEffectProbability = 1;
		}
	}
}
