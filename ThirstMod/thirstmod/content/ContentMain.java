package net.minecraft.src.thirstmod.content;

import java.io.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.src.thirstmod.Utilities;

public class ContentMain {
	private static List addedFiles = new ArrayList();
	
	public static int id;
	public static String name;
	public static String shortName;
	public static String icon;
	public static int maxStackSize;
	
	public void prepare() {
		File contentDir = new File(Minecraft.getMinecraftDir(), "/mods/ThirstMod/Content/");
		boolean deleted = false;
		if (!contentDir.exists()) {
			contentDir.mkdir();
			return;
		}
		List<File> filesList = new ArrayList<File>();
		for (File file : contentDir.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}
			filesList.add(file);
		}
		for (File file : filesList) {
			File[] files = contentDir.listFiles();
			if (files == null) {
				Utilities.print("No files found.");
			} else {
				for (int i = 0; i < files.length; i++) {
					if (deleted == false) {
						Utilities.deleteFiles(Minecraft.getMinecraftDir() + "/mods/ThirstMod/Content/", ".DS_Store");
						Utilities.deleteFiles(Minecraft.getMinecraftDir() + "/mods/ThirstMod/Content/", ".ini");
						deleted = true;
					}
					if(!addedFiles.contains(files[i].getName()) && deleted == true) {
						setOriginal();
						load();
					}
				}
			}
		}
	}
	
	public void load() {
		
	}
	
	public void init(BufferedReader br) {
		while (true)  {
			String line = null;
			try {
				line = br.readLine();
			} 
			catch (Exception e) {
				break;
			}
			if (line == null) {
				break;
			}
			if (line.startsWith("//"))
				continue;
			String[] colon = line.split(":");
			if (colon.length < 2)
				continue;
			readFromFile(colon);
		} 
	}
	
	public void readFromFile(String[] text) {
		if(text[0].equals("ID")) {
			id = Integer.parseInt(text[1]);
		}
		if (text[0].equals("Name")) {
			name = text[1];
			for (int i = 0; i < text.length - 2; i++) {
				name = (name + " " + text[(i + 2)]);
	        }
		}
		if (text[0].equals("ShortName")) {
			shortName = text[1];
		}
		if(text[0].equals("Texture")) {
			icon = "/Content Textures/" + text[1];
		}
		if (text[0].equals("MaxStackSize")) {
			maxStackSize = Integer.parseInt(text[1]);
		}
	}
	
	public void setOriginal() {
		id = 0;
		name = null;
		shortName = null;
		icon = null;
		maxStackSize = 0;
	}
}
