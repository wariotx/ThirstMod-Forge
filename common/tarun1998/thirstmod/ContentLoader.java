package tarun1998.thirstmod;

import java.io.*;
import net.minecraft.src.*;
import java.util.*;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.MinecraftForgeClient;

public class ContentLoader {
	private static List addedfiles = new ArrayList();
	private String textureFile;
	private String fileName;

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

	public ContentLoader(Side side) {
		loadMainFiles(side);
	}

	public void loadMainFiles(Side side) {
		boolean deletedBad = false;
		File contentDir = new File(ThirstUtils.getDir(), "/mods/ThirstMod/Content/");
		contentDir.mkdirs();

		if (deletedBad == false) {
			ThirstUtils.deleteFiles(ThirstUtils.getDir() + "/mods/ThirstMod/Content/", ".DS_Store");
			ThirstUtils.deleteFiles(ThirstUtils.getDir() + "/mods/ThirstMod/Content/", ".ini");
			deletedBad = true;
		}

		if (deletedBad == true) {
			LinkedList<File> contentList = new LinkedList<File>();
			LinkedList<String> drinkList = new LinkedList<String>();
			if (contentDir.listFiles().length > 0) {
				for (int i = 0; i < contentDir.listFiles().length; i++) {
					if (contentDir.listFiles()[i].getName() != "Textures") {
						contentList.add(contentDir.listFiles()[i]);
					}
				}
			}

			for (int i = 0; i < contentList.size(); i++) {
				File files = contentList.get(i);
				ThirstUtils.deleteFiles(files.getAbsolutePath(), ".DS_Store");
				ThirstUtils.deleteFiles(files.getAbsolutePath(), ".ini");

				for (int j = 0; j < files.listFiles().length; j++) {
					File fileInDir = files.listFiles()[j];
					if (fileInDir.getName().endsWith(".txt")) {
						drinkList.add(fileInDir.getName());
						try {
							init(new BufferedReader(new FileReader(fileInDir)), files.getName());

							Item drink = (((Drink) new Drink(id, alwaysDrinkable).setItemName(shortName).setMaxStackSize(maxStackSize)).setEffect(isShiny)).setPoisoningChance(chance).setPotionEffect(potionId, potionDuration, potionDuration, potionEffectProbability).setTexFile(textureFile);

							drink.setIconIndex(texPos);
							LanguageRegistry.addName(drink, name);
							ThirstUtils.addJMRecipe(jmTop, metadata, new ItemStack(drink));
							DrinkController.addDrink(drink, replenish, saturation);

							if (side.equals(Side.CLIENT)) {
								MinecraftForgeClient.preloadTexture(textureFile);
							}
							String output = fileInDir.getName().replace(".txt", "");
							addedfiles.add(output);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			ThirstUtils.print("Loaded the following drinks: " + addedfiles.toString().replace("[", "").replace("]", ""));
		}
	}

	public void init(BufferedReader br, String fileDir) {
		while (true) {
			String line = null;
			try {
				line = br.readLine();
			} catch (Exception e) {
				break;
			}
			if (line == null) {
				break;
			}
			String[] colon = line.split(":");
			read(colon, fileDir);
		}
	}

	/**
	 * Reads the text file.
	 * 
	 * @param colon The character that all the information is written after. In
	 * this case ':'
	 */
	public void read(String[] colon, String fileDir) {
		/*APIHooks.onRead(fileName, colon, getClass());*/
		if (colon[0].equals("ID")) {
			id = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("TextureFile")) {
			textureFile = "/Content/" + fileDir + "/" + colon[1];
		}
		if (colon[0].equals("TexturePos")) {
			texPos = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("Replenish")) {
			replenish = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("Saturation")) {
			saturation = Float.parseFloat(colon[1]);
		}
		if (colon[0].equals("Name")) {
			name = colon[1];
			for (int i = 0; i < colon.length - 2; i++) {
				name = (name + " " + colon[(i + 2)]);
			}
		}
		if (colon[0].equals("ShortName")) {
			shortName = colon[1];
		}
		if (colon[0].equals("DBRecipe")) {
			jmTop = Integer.parseInt(colon[1]);
			metadata = Integer.parseInt(colon[2]);
		}
		if (colon[0].equals("AlwaysDrinkable")) {
			alwaysDrinkable = Boolean.parseBoolean(colon[1].toLowerCase());
		}
		if (colon[0].equals("MaxStackSize")) {
			maxStackSize = Integer.parseInt(colon[1]);
		}
		if (colon[0].equals("PoisonChance")) {
			poisonChance = Float.parseFloat(colon[1]);
		}
		if (colon[0].equals("Shiny")) {
			isShiny = Boolean.parseBoolean(colon[1].toLowerCase());
		}
		if (colon[0].equals("FoodHeal")) {
			foodHeal = Integer.parseInt(colon[1]);
			satHeal = Float.parseFloat(colon[2]);
		}
		if (colon[0].equals("PotionEffect")) {
			potionId = Integer.parseInt(colon[1]);
			potionDuration = Integer.parseInt(colon[2]);
			potionAmplifier = Integer.parseInt(colon[3]);
			potionEffectProbability = 1;
		}
	}
}
