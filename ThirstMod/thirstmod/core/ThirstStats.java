/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.core;

import java.util.*;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.other.*;

public class ThirstStats {
	public int level;
	public float exhaustion;
	public float saturation;
	public int healTimer;
	public boolean poisoned;
	public int shiftGain;
	
	public Random random;
	public int poisonTimer;
	
	private String worldName;
	private String prevWorldName;
	private boolean readNbt;
	private boolean isNew;
	public static List biomes = new ArrayList();
	public static float poisonRate;
	
	public ThirstStats() {
		level = 20;
		exhaustion = 0.0f;
		saturation = 5f;
		healTimer = 0;
		poisoned = false;
		shiftGain = 0;
		
		random = new Random();
		poisonTimer = 0;
		
		readNbt = false;
		isNew = false;
	}
	
	public void onUpdate(EntityPlayer player) {
		int difficulty;
		worldName = mod_ThirstMod.getSaveName();
		setupNbt(player);
		if(ConfigHelper.peacefulOn == true) {
			difficulty = 1;
		}
		else {
			difficulty = player.worldObj.difficultySetting;
		}
		if(exhaustion > 4f) {
			exhaustion -= 4f;
			if(saturation > 0.0f) {
				saturation = Math.max(saturation - 1.0f, 0.0f);
			}
			else if(difficulty > 0) {
				level = Math.max(level - 1, 0);
			}
		}
		if(level <= 0) {
			healTimer++;
			if(healTimer >= 80) {
				if (player.getHealth() > 10 || difficulty >= 3 || player.getHealth() > 1 && difficulty >= 2) {
                    player.attackEntityFrom(DamageSource.starve, 1);
                }
				healTimer = 0;
			}
		}
		if(level > 18 && player.shouldHeal()) {
			healTimer++;
			if(healTimer >= 80) {
				player.heal(poisonTimer);
				healTimer = 0;
			}
		}
		if(poisoned == true) {
			initPoison();
		}
		exhaustPlayer();
		replenishPlayer();
	}
	
	public void exhaustPlayer() {
		EntityPlayer player = ModLoader.getMinecraftInstance().thePlayer;
		int multiplier = Utilities.getCurrentBiome(player) == "Desert" ? 2 : 1;
		int movement = Utilities.getMovementStat(player);
		float tweak = (float)ConfigHelper.thirstRate / 10;
		if (player.isInsideOfMaterial(Material.water)) {
			if (movement > 0) {
				Utilities.getStats().addExhaustion(0.015F * (float) movement * 0.003F * tweak);
			}
		} else if (player.isInWater()) {
			if (movement > 0) {
				Utilities.getStats().addExhaustion(0.015F * (float) movement * 0.003F * tweak);
			}
		} else if (player.onGround) {
			if (movement > 0) {
				if (player.isSprinting()) {
					Utilities.getStats().addExhaustion(0.09999999F * (float) movement * 0.02F * multiplier * tweak);
				} else {
					Utilities.getStats().addExhaustion(0.01F * (float) movement * 0.02F * multiplier * tweak);
				}
			}
		} else if (mod_ThirstMod.isPlayerJumping()) {
			if (player.isSprinting()) {
				Utilities.getStats().addExhaustion(0.04F * multiplier * tweak);
			} else {
				Utilities.getStats().addExhaustion(0.004F * multiplier * tweak);
			}
		} else {
			Utilities.getStats().addExhaustion(0.160f);
		}
	}
	
	public void initPoison() {
		poisonTimer++;
		addExhaustion(0.05f);
		if(poisonTimer >= 300) {
			poisoned = false;
			poisonTimer = 0;
		}
	}
	
	public void replenishPlayer() {
		EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
		if (entityplayer.isInWater() && entityplayer.isSneaking()
				&& !entityplayer.worldObj.isRemote) {
			if (needWater() == true) {
				if (shiftGain == 0) {
					entityplayer.worldObj.playSoundAtEntity(entityplayer, "random.drink", 0.5F, entityplayer.worldObj.rand.nextFloat() * 0.1F + 0.9F);
					addStats(1, 0.3f);
					if (Utilities.getCurrentBiome(entityplayer) == "Ocean") {
						if (random.nextFloat() < 0.6f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Plains") {
						if (random.nextFloat() < 0.3f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Desert") {
						if (random.nextFloat() < 0f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Extreme Hills") {
						if (random.nextFloat() < 0.4f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Forest") {
						if (random.nextFloat() < 0.5f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Taiga") {
						if (random.nextFloat() < 0.4f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Swampland") {
						if (random.nextFloat() < 0.8f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "River") {
						if (random.nextFloat() < 0.2f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "FrozenOcean") {
						if (random.nextFloat() < 0.2f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Ice Plains") {
						if (random.nextFloat() < 0.2f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Ice Mountains") {
						if (random.nextFloat() < 0.2f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "MushroomIsland") {
						if (random.nextFloat() < 0.5f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "MushroomIslandShore") {
						if (random.nextFloat() < 0.6f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "Beach") {
						if (random.nextFloat() < 0.6f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "DesertHills") {
						if (random.nextFloat() < 0f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					if (Utilities.getCurrentBiome(entityplayer) == "ForestHills") {
						if (random.nextFloat() < 0.4f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					} 
					if(biomes.contains(Utilities.getCurrentBiome(entityplayer))) {
						if (random.nextFloat() < poisonRate && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					} else {
						if (random.nextFloat() < 0.3f && ConfigHelper.poisonOn) {
							poisoned = true;
						}
					}
					shiftGain = 16;
				} else {
					shiftGain--;
				}
			}
		}
	}
	
	public void setupNbt(EntityPlayer player) {
		if(worldName != prevWorldName) {
			readNbt = true;
			if(player.worldObj.isNewWorld) {
				isNew = true;
			}
			prevWorldName = worldName;
		}
		if(isNew == true) {
			SaveHandlerMod mod = new SaveHandlerMod();
			mod.writeNbt();
		}
		if(readNbt == true) {
			SaveHandlerMod mod = new SaveHandlerMod();
			mod.readNbt();
			readNbt = false;
		}
	}
	
	public void addStats(int amount, float saturation) {
		level = level + amount;
	}
	
	public void addExhaustion(float amount) {
		exhaustion = Math.min(exhaustion + amount, 40.0F);
    }
	
	public boolean needWater() {
		return level < 20;
	}
}
