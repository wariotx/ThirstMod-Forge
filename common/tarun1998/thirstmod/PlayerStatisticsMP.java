package tarun1998.thirstmod;

import java.util.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.src.*;

public class PlayerStatisticsMP extends PlayerStatistics {
	public int level;
	public float saturation;
	public float exhaustion;
	public int healhurtTimer;
	public int drinkTimer;
	public int serverHurt;
	private Random random = new Random();
	private PoisonController poisonCon = new PoisonController();

	public PlayerStatisticsMP() {
		level = 20;
		saturation = 5f;
		exhaustion = 0f;
		healhurtTimer = 0;
		drinkTimer = 0;
		serverHurt = 0;
	}

	/**
	 * Holds the Thirst Logic. Controls everything related to that Thirst Bar.
	 * @param player EntityPlayer instance.
	 */
	public void onTick(EntityPlayer player, EntityPlayerMP playerMp) {
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
		if (level == 0) {
			serverHurt++;
			if (serverHurt > 80) {
				if (playerMp.getHealth() > 10 || difSet >= 3 || playerMp.getHealth() > 1 && difSet >= 2) {
					serverHurt = 0;
					playerMp.attackEntityFrom(DamageSource.starve, 1);
				}
			}
		}
		poisonCon.onTick();
		if (player.isSneaking() && player.isInWater() && level < 20) {
			drinkTimer++;
			if (drinkTimer > 16) {
				addStats(1, 0.3F);
				if (poisonCon.getBiomesList().containsKey(ThirstUtils.getCurrentBiome(player)) && ConfigHelper.poisonOn == true) {
					if (random.nextFloat() < poisonCon.getBiomePoison(ThirstUtils.getCurrentBiome(playerMp))) {
						PoisonController.startPoison();
					}
				}
				drinkTimer = 0;
			}
		}
		if (level <= 6) {
			player.setSprinting(false);
		}
		exhaustPlayer(player);
	}

	/**
	 * Exhausts the player.
	 * @param player EntityPlayer Instance.
	 */
	public void exhaustPlayer(EntityPlayer player) {
		int multiplier = ThirstUtils.getCurrentBiome(player) == "Desert" ? 2 : 1;
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
		} else {
			addExhaustion(0.160f);
		}
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
