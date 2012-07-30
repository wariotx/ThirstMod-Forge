package net.minecraft.src.ThirstMod.core;

import java.io.*;
import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.guis.GuiThirst;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class ThirstModCore 
{
	private Random random = new Random();
	private boolean gameInitialised = false;
	private int attempts = 0;
	public static int duration = 0;
	public static int time = 360;
	
	public static List biomes = new ArrayList();
	public static float poisonRate;
	
	public ThirstModCore() 
	{
	}
	
	public boolean onTickInGame(float f, Minecraft minecraft)
    {
		if(!minecraft.thePlayer.capabilities.isCreativeMode && mod_ThirstMod.thirstOn == true) 
		{
			if(!minecraft.theWorld.isRemote)
			{
				ThirstUtils.getStats().onUpdate(minecraft.thePlayer);
			}
			applyExhaustion();
			replenishThirst();
			
			if(ThirstUtils.getStats().isThirstPoisoned == true)
			{
				performThirstPoison();
			}
			if(gameInitialised == false)
			{
				minecraft.ingameGUI = new GuiThirst(minecraft);
				gameInitialised = true;
			}
			if (minecraft.theWorld.isNewWorld && attempts == 0 && !minecraft.theWorld.isRemote)
			{
				minecraft.thePlayer.inventory.addItemStackToInventory(new ItemStack(mod_ThirstMod.canteen, 1, 10));
				if (!datExist())
				{
					ThirstUtils.makeDefault();
					writeNBTToFile();
					System.out.println("New world found, dat created");
				}
				readNBTFromFile();
				attempts = attempts + 1;
			}
			if (attempts == 0 && !minecraft.theWorld.isRemote)
			{
				if (!datExist())
				{
					ThirstUtils.makeDefault();
					writeNBTToFile();
					System.out.println("New world found, dat created");
				}
				readNBTFromFile();
				attempts = attempts + 1;
			}
			int tickSave = 0;
			tickSave++;
			if(tickSave >= 100)
			{
				writeNBTToFile();
				tickSave = 0;
			}
		}
        return true;
    }

    public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen)
    {
    	if(guiscreen instanceof GuiMainMenu)
    	{
    		attempts = 0;
    	}
    	if (guiscreen instanceof GuiIngameMenu)
		{
			if (!minecraft.theWorld.isRemote)
			{
				writeNBTToFile();
			}
		}
    	if (guiscreen instanceof GuiGameOver)
		{
			// Used to fix a bug of the stats not resetting.
			ThirstUtils.makeDefault();
		}
        return true;
    }
    
    public void applyExhaustion()
    {
    	EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
    	int multiplier = ThirstUtils.getCurrentBiome(entityplayer) == "Desert" ? 2 : 1;
		float tweak = (float)mod_ThirstMod.thirstRate / 10;
		int stat = ThirstUtils.getMovementStat(entityplayer);
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
			} else if (mod_ThirstMod.isJumping())
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
    	EntityPlayer entityplayer = ModLoader.getMinecraftInstance().thePlayer;
    	if (entityplayer.isInWater() && entityplayer.isSneaking() && !entityplayer.worldObj.isRemote)
		{
			if (ThirstUtils.getStats().needWater() == true)
			{
				if (duration == 0)
				{
					entityplayer.worldObj.playSoundAtEntity(entityplayer, "random.drink", 0.5F, entityplayer.worldObj.rand.nextFloat() * 0.1F + 0.9F);
					ThirstUtils.getStats().addStats(1, 0.3f);
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Ocean")
					{
						if (random.nextFloat() < 0.6f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Plains")
					{
						if (random.nextFloat() < 0.3f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Desert")
					{
						if (random.nextFloat() < 0f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Extreme Hills")
					{
						if (random.nextFloat() < 0.4f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Forest")
					{
						if (random.nextFloat() < 0.5f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Taiga")
					{
						if (random.nextFloat() < 0.4f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Swampland")
					{
						if (random.nextFloat() < 0.8f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "River")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "FrozenOcean")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Ice Plains")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Ice Mountains")
					{
						if (random.nextFloat() < 0.2f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "MushroomIsland")
					{
						if (random.nextFloat() < 0.5f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "MushroomIslandShore")
					{
						if (random.nextFloat() < 0.6f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "Beach")
					{
						if (random.nextFloat() < 0.6f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "DesertHills")
					{
						if (random.nextFloat() < 0f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					if (ThirstUtils.getCurrentBiome(entityplayer) == "ForestHills")
					{
						if (random.nextFloat() < 0.4f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					} else
					if (biomes.contains(ThirstUtils.getCurrentBiome(entityplayer)))
					{	
						if (random.nextFloat() < poisonRate && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					} else
					{
						if (random.nextFloat() < 0.3f && mod_ThirstMod.isThirstPoisoningOn)
						{
							ThirstUtils.getStats().isThirstPoisoned = true;
						}
					}
					duration = 16;
				} else
				{
					duration--;
				}
			}
		}
    	//Removed for now.
		/*Minecraft minecraft = ModLoader.getMinecraftInstance();
		if (minecraft.theWorld.isRaining() && minecraft.thePlayer.isSneaking() && ThirstUtils.getStats().thirstHang > 80 && !(ThirstUtils.getStats().thirstLevel >= 20) && 
			entityplayer.worldObj.canBlockSeeTheSky(MathHelper.floor_double(entityplayer.posX), MathHelper.floor_double(entityplayer.posY), MathHelper.floor_double(entityplayer.posZ)))
		{
			entityplayer.worldObj.playSoundAtEntity(entityplayer, "random.drink", 0.5F, entityplayer.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			ThirstUtils.getStats().addStats(1, 0.1F);
			ThirstUtils.getStats().thirstHang = 0;
		}*/

    }
    
    public void performThirstPoison()
	{
		if (time > 0)
		{
			if (time % 60 == 0)
			{
				ThirstUtils.addExhaustion(0.083333333333333f);
			}
			time--;
		} else
		{
			ThirstUtils.getStats().isThirstPoisoned = false;
			time = 360;
		}
	}
    
    private void writeNBTToFile()
	{
		try
		{
			File file = new File(ModLoader.getMinecraftInstance().getMinecraftDir() + "/saves/" + mod_ThirstMod.getSaveName() + "", "Thirst.dat");
			file.createNewFile();
			if (!file.exists())
			{
				return;
			}
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("thirstLevel", ThirstUtils.getStats().thirstLevel);
			nbttagcompound.setInteger("thirstTickTimer", ThirstUtils.getStats().thirstTickTimer);
			nbttagcompound.setFloat("thirstSaturationLevel", ThirstUtils.getStats().thirstSaturationLevel1);
			nbttagcompound.setFloat("thirstExhaustionLevel", ThirstUtils.getStats().thirstExhaustionLevel);
			nbttagcompound.setBoolean("isThirstPoisoned", ThirstUtils.getStats().isThirstPoisoned);
			nbttagcompound.setInteger("thirstPoisonCountdown", time);
			nbttagcompound.setInteger("thirstHang", ThirstUtils.getStats().thirstHang);
			CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
			fileoutputstream.close();
		} catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	private void readNBTFromFile()
	{
		ModLoader.getMinecraftInstance().theWorld.checkSessionLock();
		try
		{
			File file = new File("" + ModLoader.getMinecraftInstance().getMinecraftDir() + "/saves/" + mod_ThirstMod.getSaveName() + "", "Thirst.dat");
			if (!file.exists())
			{
				return;
			}
			FileInputStream fileinputstream = new FileInputStream(file);
			NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
			if(nbttagcompound.hasKey("thirstLevel"))
	        {
				ThirstUtils.getStats().thirstLevel = nbttagcompound.getInteger("thirstLevel");
				ThirstUtils.getStats().thirstTickTimer = nbttagcompound.getInteger("thirstTickTimer");
				ThirstUtils.getStats().thirstSaturationLevel1 = nbttagcompound.getFloat("thirstSaturationLevel");
				ThirstUtils.getStats().thirstExhaustionLevel = nbttagcompound.getFloat("thirstExhaustionLevel");
				ThirstUtils.getStats().isThirstPoisoned = nbttagcompound.getBoolean("isThirstPoisoned");
	            time = nbttagcompound.getInteger("thirstPoisonCountdown");
	            //thirstHang = nbttagcompound.getInteger("thirstHang");
	        }
	        System.out.println("Read!");
			fileinputstream.close();
		} catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	public static boolean datExist()
	{
		File file = new File(ModLoader.getMinecraftInstance().getMinecraftDir() + "/saves/" + mod_ThirstMod.getSaveName() + "", "Thirst.dat");
		return file.exists();
	}
}