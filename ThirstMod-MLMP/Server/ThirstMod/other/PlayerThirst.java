package net.minecraft.src.ThirstMod.other;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.containers.TileEntityWaterCollector;
import net.minecraft.src.ThirstMod.core.PacketThirst;
import net.minecraft.src.ThirstMod.core.ThirstStats;
import net.minecraft.src.ThirstMod.core.ThirstUtils;

public class PlayerThirst extends PlayerBase
{
	private boolean addExhaustion = false;
	private Random random = new Random();
	private boolean gameInitialised = false;
	private int attempts = 0;
	public static int duration = 0;
	public static int time = 360;
	private static ThirstStats thirstStats;
	public static EntityPlayerMP epm;

	public static List biomes = new ArrayList();
	public static float poisonRate;

	public PlayerThirst(PlayerAPI playerapi)
	{
		super(playerapi);
		thirstStats = new ThirstStats();
		epm = player;
	}

	public void onUpdate()
	{
		super.onUpdate();
		if (!player.capabilities.isCreativeMode && mod_ThirstMod.thirstOn == true)
		{
			getStats().onUpdate(this);
			applyExhaustion();
			replenishThirst();

			if (getStats().isThirstPoisoned == true)
			{
				performThirstPoison();
			}
			sendThirstStats(PlayerThirst.getPlayer());
		}
	}
	
	public void sendThirstStats(PlayerThirst player)
	{
		int[] dataInt = new int[5];
		dataInt[0] = player.getStats().thirstLevel;
		dataInt[1] = player.getStats().thirstTickTimer;
		dataInt[2] = player.getStats().thirstDuration;
		dataInt[3] = player.getStats().isThirstPoisoned ? 1 : 0;
		dataInt[4] = player.time;
		
		float[] dataFloat = new float[2];
		dataFloat[0] = player.getStats().thirstSaturationLevel1;
		dataFloat[1] = player.getStats().thirstExhaustionLevel;

		Packet230ModLoader packet = new Packet230ModLoader();
		packet.packetType = 0;
		packet.dataInt = dataInt;
		packet.dataFloat = dataFloat;
		ModLoaderMp.sendPacketTo(mod_ThirstMod.mod, player.player, packet);
	}
	
	public void applyExhaustion()
    {
    	EntityPlayer entityplayer = player;
    	int multiplier = ThirstUtils.getCurrentBiome(entityplayer) == "Desert" ? 2 : 1;
		float tweak = (float)mod_ThirstMod.thirstRate / 10;
		int stat = getStats().instMove;
		if (mod_ThirstMod.thirstOn == true)
		{
			if (entityplayer.isInsideOfMaterial(Material.water))
			{
				if (stat > 0)
				{
					ThirstUtils.addExhaustion(0.015F * (float) stat * 0.003F * tweak);
				}
			} else if (entityplayer.isInWater())
			{
				if (stat > 0)
				{
					ThirstUtils.addExhaustion(0.015F * (float) stat * 0.003F * tweak);
				}
			} else if (entityplayer.onGround)
			{
				if (stat > 0)
				{
					if (entityplayer.isSprinting())
					{
						ThirstUtils.addExhaustion(0.09999999F * (float) stat * 0.02F * multiplier * tweak);
					} else
					{
						ThirstUtils.addExhaustion(0.01F * (float) stat * 0.02F * multiplier * tweak);
					}
				}
			} else if (player.isAirBorne)
			{
				if (entityplayer.isSprinting())
				{
					ThirstUtils.addExhaustion(0.04F * multiplier * tweak);
				} else
				{
					ThirstUtils.addExhaustion(0.004F * multiplier * tweak);
				}
			}
		} else
		{
			ThirstUtils.addExhaustion(0.0160F);
		}
    }

	public void replenishThirst()
	{
		if (player.isInWater() && player.isSneaking())
		{
			if (getStats().needWater() == true)
			{
				if (duration == 0)
				{
					player.worldObj.playSoundAtEntity(player, "random.drink", 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
					getStats().addStats(1, 0.3f);
					if (ThirstUtils.getCurrentBiome(player) == "Ocean")
					{
						if (random.nextFloat() < 0.6f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Plains")
					{
						if (random.nextFloat() < 0.3f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Desert")
					{
						if (random.nextFloat() < 0f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Extreme Hills")
					{
						if (random.nextFloat() < 0.4f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Forest")
					{
						if (random.nextFloat() < 0.5f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Taiga")
					{
						if (random.nextFloat() < 0.4f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Swampland")
					{
						if (random.nextFloat() < 0.8f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "River")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "FrozenOcean")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Ice Plains")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Ice Mountains")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "MushroomIsland")
					{
						if (random.nextFloat() < 0.5f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "MushroomIslandShore")
					{
						if (random.nextFloat() < 0.6f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "Beach")
					{
						if (random.nextFloat() < 0.6f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "DesertHills")
					{
						if (random.nextFloat() < 0f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(player) == "ForestHills")
					{
						if (random.nextFloat() < 0.4f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					} else if (biomes.contains(ThirstUtils.getCurrentBiome(player)))
					{
						if (random.nextFloat() < poisonRate && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					} else
					{
						if (random.nextFloat() < 0.3f && mod_ThirstMod.isThirstPoisoningOn)
						{
							getStats().isThirstPoisoned = true;
						}
					}
					duration = 16;
				} else
				{
					duration--;
				}
			}
		}
		
		//Removed for now causing problems.
		/*if (player.worldObj.isRaining() && player.isSneaking() && getStats().thirstHang > 80 && !(getStats().needWater()) && player.worldObj.canBlockSeeTheSky(player.chunkCoordX, player.chunkCoordY, player.chunkCoordZ));
		{
			player.worldObj.playSoundAtEntity(player, "random.drink", 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			getStats().addStats(1, 0.3F);
			getStats().thirstHang = 0;
		}*/

	}

	public void performThirstPoison()
	{
		if (time > 0)
		{
			if (time % 60 == 0)
			{
				getStats().addExhaustion(0.083333333333333f);
			}
			time--;
		} else
		{
			getStats().isThirstPoisoned = false;
			time = 360;
		}
	}

	public void attackTargetEntityWithCurrentItem(Entity entity)
	{
		super.attackTargetEntityWithCurrentItem(entity);
		getStats().addExhaustion(0.6f);
	}

	public void damageEntity(DamageSource damagesource, int i)
	{
		super.damageEntity(damagesource, i);
		getStats().addExhaustion(0.6f);
	}

	public boolean canHarvestBlock(Block block)
	{
		if (block == Block.obsidian)
		{
			getStats().addExhaustion(0.1f);
			// I don't know why i added this.
		}
		getStats().addExhaustion(0.034f);
		return super.canHarvestBlock(block);
	}
	
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) 
	{
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("thirstLevel", getStats().thirstLevel);
		nbttagcompound.setInteger("thirstTickTimer", getStats().thirstTickTimer);
		nbttagcompound.setFloat("thirstSaturationLevel", getStats().thirstSaturationLevel1);
		nbttagcompound.setFloat("thirstExhaustionLevel", getStats().thirstExhaustionLevel);
		nbttagcompound.setBoolean("isThirstPoisoned", getStats().isThirstPoisoned);
		nbttagcompound.setInteger("thirstPoisonCountdown", time);
		nbttagcompound.setInteger("thirstHang", getStats().thirstHang);
	}
	
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) 
	{
		super.readEntityFromNBT(nbttagcompound);
		if(nbttagcompound.hasKey("thirstLevel"))
        {
			getStats().thirstLevel = nbttagcompound.getInteger("thirstLevel");
			getStats().thirstTickTimer = nbttagcompound.getInteger("thirstTickTimer");
			getStats().thirstSaturationLevel1 = nbttagcompound.getFloat("thirstSaturationLevel");
			getStats().thirstExhaustionLevel = nbttagcompound.getFloat("thirstExhaustionLevel");
			getStats().isThirstPoisoned = nbttagcompound.getBoolean("isThirstPoisoned");
            time = nbttagcompound.getInteger("thirstPoisonCountdown");
        }
	}
	
	public static ThirstStats getStats()
	{
		return thirstStats;
	}
	
	public static PlayerThirst getPlayer()
	{
		return (PlayerThirst) epm.getPlayerBase("ThirstMod");
	}
}