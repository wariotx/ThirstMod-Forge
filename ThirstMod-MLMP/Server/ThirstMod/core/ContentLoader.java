package net.minecraft.src.ThirstMod.core;

import java.io.*;
import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.core.ThirstModCore;
import net.minecraft.src.ThirstMod.core.ThirstUtils;
import net.minecraft.src.ThirstMod.drinks.Drink;
import net.minecraft.src.ThirstMod.other.PlayerThirst;

import java.util.ArrayList;
import java.util.List;

public class ContentLoader
{
	private static List addedDrinks = new ArrayList();
	
	public static int id;
	public static int texture;
	public static int replenish;
	public static int jmTop;
	public static int jmResult;
	public static float saturation;
	public static String name;
	public static String shortName;
	public static String icon;
	public static boolean alwaysDrinkable;
	public static boolean hasEffect;
	public static int maxStackSize;
	public static float poisonChance;
	public static boolean isShiny;
	public static String type;
	public static int chance;
	public static int foodHeal;
	public static float satHeal;
	
	public static int potionId;
	public static int potionDuration;
	public static int potionAmplifier;
	public static float potionEffectProbability;
	
	public static void loadContent()
	{
		File apiDir = new File(ThirstUtils.getDir() + "/mods/ThirstMod/Content/");

		boolean deleted = false;
		
		if (!apiDir.exists())
		{
			apiDir.mkdir();
			return;
		}
		List<File> drinksList = new ArrayList<File>();
		for (File file : apiDir.listFiles())
		{
			if (file.isDirectory())
			{
				continue;
			}
			drinksList.add(file);
		}
		for (File file : drinksList)
		{
			File[] drinks = apiDir.listFiles();
			if (drinks == null)
			{
				ThirstUtils.print("No Drinks found.");
			} else
			{
				for (int i = 0; i < drinks.length; i++)
				{
					if (deleted == false)
					{
						ThirstUtils.deleteFiles(ThirstUtils.getDir() + "/mods/ThirstMod/Content/", ".DS_Store");
						ThirstUtils.deleteFiles(ThirstUtils.getDir() + "/mods/ThirstMod/Content/", ".ini");
						deleted = true;
					}
					if (!addedDrinks.contains(drinks[i].getName()) && deleted == true)
					{
						try
						{
							init(new BufferedReader(new FileReader(drinks[i])));
							
							Item drink = (((Drink) new Drink(id, replenish, saturation, alwaysDrinkable).setItemName(shortName)
									.setMaxStackSize(maxStackSize)).setEffect(isShiny)).setPoisoningChance(chance)
									.setPotionEffect(potionId, potionDuration, potionDuration, potionEffectProbability);
							
							addedDrinks.add(drinks[i].getName());

							ThirstUtils.addJuiceRecipe(jmTop, new ItemStack(drink));
						} catch (Exception e)
						{
							ThirstUtils.print("Drink not loaded:" + drinks[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
		File template = new File(ThirstUtils.getDir() + "/mods/ThirstMod/Template.txt");
		if (!template.exists())
		{
			try
			{
				template.createNewFile();
				FileOutputStream fos = new FileOutputStream(template);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "8859_1"));
				
				bw.write("//The ID of the drink" + "\r\n");
				bw.write("ID:" + "\r\n");
				bw.write("//Ingame Name" + "\r\n");
				bw.write("Name:" + "\r\n");
				bw.write("//Java Name" + "\r\n");
				bw.write("ShortName:" + "\r\n");
				bw.write("//The texture of the drink, place in /minecraft.jar/ThirstMod/Images/." + "\r\n");
				bw.write("Texture:" + "\r\n");
				bw.write("//Amount replenished when drunk" + "\r\n");
				bw.write("Replenish:" + "\r\n");
				bw.write("//When this is equal to zero, the meter drops" + "\r\n");
				bw.write("Saturation:" + "\r\n");
				bw.write("//The item that creates this drink in the Drinks Brewer" + "\r\n");
				bw.write("DBRecipe:" + "\r\n\r\n");
				bw.write("//If it is always drinkable, true or false" + "\r\n");
				bw.write("AlwaysDrinkable:" + "\r\n");
				bw.write("//The max stack size" + "\r\n");
				bw.write("MaxStackSize:" + "\r\n");
				bw.write("//Poison Chance when drunk, use numbers 0.1 - 1. EG. 0.1 = 10% chance of poison" + "\r\n");
				bw.write("PoisonChance:" + "\r\n");
				bw.write("//If the drink shines like potions" + "\r\n");
				bw.write("Shiny:" + "\r\n\r\n");
				bw.write("If the drink can heal the Food Bar: var = Amount Healed, var1 = Saturation Healed. Look at the wiki on how the saturation works." + "\r\n");
				bw.write("FoodHeal:var:var1" + "\r\n");
				bw.write("You must have all the above stuff to make sure all the drinks will work!" + "\r\n");
				bw.write("Look at the Mo' Drinks content pack for more infomation.");
				
				bw.close();
				fos.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		File customBiomePoisoning = new File(ThirstUtils.getDir() + "/mods/ThirstMod/Custom Biome Posioning.txt");
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

				PlayerThirst.biomes.add(split[0]);
				PlayerThirst.poisonRate = Float.parseFloat(split[1]);
				// log(biome + poisonRate);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		String loaded = addedDrinks.toString().replace("[", "").replace("]", "").replace(".txt", "");
		ThirstUtils.print("Loaded:" + loaded);
	}
	
	public static void init(BufferedReader br)
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
	
	public static void read(String[] colon)
	{
		if (colon[0].equals("ID"))
		{
			id = Integer.parseInt(colon[1]);
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
		if(colon[0].equals("Texture"))
		{
			icon = "/Content Textures/" + colon[1];
		}
		if (colon[0].equals("DBRecipe"))
		{
			jmTop = Integer.parseInt(colon[1]);
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
