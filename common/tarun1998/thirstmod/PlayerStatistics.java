package tarun1998.thirstmod;

import java.util.*;

import tarun1998.thirstmod.api.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import tarun1998.thirstmod.packets.PacketPlaySound;
import tarun1998.thirstmod.utils.*;

public class PlayerStatistics {
	public int level;
	public float saturation;
	public float exhaustion;
	public int healhurtTimer;
	public int drinkTimer;
	public int rainTimer;
	private Random random = new Random();
	private PoisonController poisonCon = new PoisonController();

	public PlayerStatistics() {
		level = 20;
		saturation = 5f;
		exhaustion = 0f;
		healhurtTimer = 0;
		drinkTimer = 0;
	}

	/**
	 * Holds the Thirst Logic. Controls everything related to that Thirst Bar.
	 * @param player EntityPlayer instance.
	 */
	public void onTick(EntityPlayer player) {
		int difSet = player.worldObj.difficultySetting;
		if (ConfigHelper.peacefulOn == true) {
			difSet = 1;
		}
		if (exhaustion > 4f) {
			exhaustion = 0f;
			if (saturation > 0f) {
				saturation = saturation - 1f;
			} else if (difSet > 0) {
				level = level - 1;
			}
		}
		if(PacketHandler.isRemote == false) {
			if (level == 0) {
				healhurtTimer++;
				if (healhurtTimer > 80) {
					if (ClientProxy.getPlayerMp().getHealth() > 10 || difSet >= 3 || ClientProxy.getPlayerMp().getHealth() > 1 && difSet >= 2) {
						healhurtTimer = 0;
						ClientProxy.getPlayerMp().attackEntityFrom(DamageSource.starve, 1);
						MinecraftForge.EVENT_BUS.post(new ThirstAPI.OnPlayerHurt(player));
					}
				}
			}
		}
		poisonCon.onTick();
		if (player.isSneaking() && player.isInWater() && level < 20) {
			drinkTimer++;
			if (drinkTimer > 16) {
				addStats(1, 0.3F);
				if(PacketHandler.isRemote == false) {
					ClientProxy.getPlayerMp().worldObj.playSoundAtEntity(player, "random.drink", 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
				}
				if (poisonCon.getBiomesList().containsKey(ThirstUtils.getCurrentBiome(player)) && ConfigHelper.poisonOn == true) {
					if (random.nextFloat() < poisonCon.getBiomePoison(ThirstUtils.getCurrentBiome(player))) {
						PoisonController.startPoison();
					}
				}
				MinecraftForge.EVENT_BUS.post(new ThirstAPI.OnPlayerDrinkWater(player));
				drinkTimer = 0;
			}
		}
		if (level <= 6) {
			player.setSprinting(false);
		}
		if (level < 0) {
			level = 0;
		}
		exhaustPlayer(player);
		drinkFromRain(player);
	}

	/**
	 * Exhausts the player.
	 * @param player EntityPlayer Instance.
	 */
	public void exhaustPlayer(EntityPlayer player) {
		int multiplier;
		if(PacketHandler.isRemote == false) {
			 multiplier = ThirstUtils.getCurrentBiome(ClientProxy.getPlayerMp()) == "Desert" ? 2 : 1;
		} else {
			 multiplier = ThirstUtils.getCurrentBiome(player) == "Desert" ? 2 : 1;
		}
		int movement = ThirstUtils.getMovementStat(player);
		float tweak = (float) ConfigHelper.thirstRate / 10;
		if (player.isInsideOfMaterial(Material.water)) {
			if (movement > 0) {
				addExhaustion(0.015F * (float) movement * 0.003F * tweak);
			}
		} else if (player.isInWater()) {
			if (movement > 0) {
				addExhaustion(0.015F * (float) movement * 0.003F * tweak);
			}
		} else if (player.onGround) {
			if (movement > 0) {
				if (player.isSprinting()) {
					addExhaustion(0.09999999F * (float) movement * 0.02F * multiplier * tweak);
				} else {
					addExhaustion(0.01F * (float) movement * 0.02F * multiplier * tweak);
				}
			}
		} else if (ThirstMod.isJumping(player)) {
			if (player.isSprinting()) {
				addExhaustion(0.04F * multiplier * tweak);
			} else {
				addExhaustion(0.004F * multiplier * tweak);
			}
		} 
	}
	
	/**
	 * Does whether the player is attempting to drink from rain.
	 * @param player
	 */
	public void drinkFromRain(EntityPlayer player) {
		int x = (int)player.posX;
		int y = (int)player.posY;
		int z = (int)player.posZ;
		
		if(player.isSneaking() == true && isPlayerTopEmpty(x, y, z, player.worldObj) == true && player.worldObj.getWorldInfo().isRaining() == true
				&& player.isInWater() == false && player.isInsideOfMaterial(Material.water) == false) {
			rainTimer++;
			if(rainTimer > 40) {
				this.addStats(1, 0.2f);
				if(PacketHandler.isRemote == false) {
					ClientProxy.getPlayerMp().worldObj.playSoundAtEntity(player, "random.drink", 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
				} 
				rainTimer = 0;
			}
		}
	}
	
	/**
	 * Checks whether there is any block above the player position.
	 * @param x position of the player.
	 * @param y position of the player.
	 * @param z position of the player.
	 * @param world World.class instance.
	 * @return false if there is a block above the player.
	 */
	public boolean isPlayerTopEmpty(int x, int y, int z, World world) {
		for(int i = y; i < 256; i++) {
			if(world.getBlockId(x, i, z) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds exhaustion
	 * @param par1 Amount to be added.
	 */
	public void addExhaustion(float par1) {
		exhaustion = Math.min(exhaustion + par1, 40F);
	}

	/**
	 * Adds stats to the level and saturation.
	 * @param par1 Amount to add to level.
	 * @param par2 Amount to saturation.
	 */
	public void addStats(int par1, float par2) {
		level = Math.min(par1 + level, 20);
		saturation = Math.min(saturation + (float) par1 * par2 * 2.0F, level);
	}
}
