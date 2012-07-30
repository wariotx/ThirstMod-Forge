package net.minecraft.src.thirstmod;

import java.io.*;
import net.minecraft.src.*;
import net.minecraft.src.forge.*;
import net.minecraft.src.thirstmod.content.ContentMain;

public class SaveHandlerMod implements ISaveEventHandler {
	
	public void onWorldLoad(World world) {
		new ContentMain().registerTextureOverrides(ModLoader.getMinecraftInstance().renderEngine);
	}

	public void onWorldSave(World world) {
		if(mod_ThirstMod.getSaveName() != null) {
			writeNbt();
		}
	}

	public void onChunkLoad(World world, Chunk chunk) {
	}

	public void onChunkUnload(World world, Chunk chunk) {
	}

	public void onChunkSaveData(World world, Chunk chunk, NBTTagCompound data) {
	}

	public void onChunkLoadData(World world, Chunk chunk, NBTTagCompound data) {
	}
	
	public void writeNbt() {
		try {
			if(mod_ThirstMod.getSaveName() != null) {
				File dat = new File(ModLoader.getMinecraftInstance().getMinecraftDir() + "/saves/" + mod_ThirstMod.getSaveName() + "", "Thirst.dat");
				dat.createNewFile();
				if(!dat.exists()) {
					throw new MinecraftException("ThirstMod's save file was never created!!!");
				}
				FileOutputStream fos = new FileOutputStream(dat);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("tmLevel", Utilities.getStats().level);
				nbt.setInteger("tmTimer", Utilities.getStats().healTimer);
				nbt.setFloat("tmExhaustion", Utilities.getStats().exhaustion);
				nbt.setFloat("tmSaturation", Utilities.getStats().saturation);
				CompressedStreamTools.writeCompressed(nbt, fos);
				fos.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readNbt() {
		try {
			if(mod_ThirstMod.getSaveName() != null) {
				File dat = new File(ModLoader.getMinecraftInstance().getMinecraftDir() + "/saves/" + mod_ThirstMod.getSaveName() + "", "Thirst.dat");
				if (!dat.exists()) {
					throw new MinecraftException("ThirstMod's save file could not be found!");
				}
				FileInputStream fis = new FileInputStream(dat);
				NBTTagCompound nbt = CompressedStreamTools.readCompressed(fis);
				if (nbt.hasKey("tmLevel")) {
					Utilities.getStats().level = nbt.getInteger("tmLevel");
					Utilities.getStats().healTimer = nbt.getInteger("tmTimer");
					Utilities.getStats().exhaustion = nbt.getFloat("tmExhaustion");
					Utilities.getStats().saturation = nbt.getFloat("tmSatauration");
				}
				fis.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
