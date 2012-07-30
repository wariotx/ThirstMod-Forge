package net.minecraft.src.ThirstMod.core;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.mod_ThirstMod;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.drinks.Drink;
import net.minecraft.src.ThirstMod.drinks.ItemWoodDrink;
import net.minecraft.src.ThirstMod.other.PlayerThirst;

public class ThirstStats 
{
	public static int thirstLevel;
    public static float thirstSaturationLevel1 = 5f;
    public float thirstExhaustionLevel = 0;
    public int thirstTickTimer;
    public int prevthirstLevel;
    public boolean isThirstPoisoned;
    public int thirstDuration;
    public int thirstHang;
    public int instMove;
    
    public ThirstStats()
    {
        thirstTickTimer = 0;
        thirstLevel = 20;
        prevthirstLevel = 20;
        isThirstPoisoned = false;
        thirstDuration = 360;
        thirstHang = 0;
        instMove = 0;
    }

    public static void addStats(int i, float f)
    {
        thirstLevel = Math.min(i + thirstLevel, 20);
        thirstSaturationLevel1 = Math.min(thirstSaturationLevel1 + (float)i * f * 2.0F, thirstLevel);
    }

    public void onUpdate(PlayerThirst entityplayer)
    {
        int i;
        //remove below when doing api
        if(mod_ThirstMod.thirstInPeaceful == true)
        {
        	i = 1;
        }
        else
        {
        	i = entityplayer.epm.worldObj.difficultySetting;
        }
        prevthirstLevel = thirstLevel;
        if(thirstExhaustionLevel > 4F)
        {
            thirstExhaustionLevel = 0f;
            if(thirstSaturationLevel1 > 0.0F)
            {
                thirstSaturationLevel1 = thirstSaturationLevel1 - 1.0f;
            } 
            if(i > 0 && thirstSaturationLevel1 <= 0)
            {
                thirstLevel = Math.max(thirstLevel - 1, 0);
            }
        }
        if(thirstLevel >= 18 && i > 0)
        {
        	if(entityplayer.epm.shouldHeal())
        	{
        		thirstTickTimer++;
        		if(thirstTickTimer >= 80)
        		{
        			entityplayer.epm.heal(1);
        			thirstTickTimer = 0;
        		}
        	}
        } 
        else
        if(thirstLevel <= 0)
        {
            thirstTickTimer++;
            if(thirstTickTimer >= 80)
            {
                if(entityplayer.epm.getHealth() > 10 || i >= 3 || entityplayer.epm.getHealth() > 1 && i >= 2)
                {
                    entityplayer.attackEntityFrom(DamageSource.starve, 1);
                }
                thirstTickTimer = 0;
            }
        } else
        {
            thirstTickTimer = 0;
        }
        if(thirstLevel <= 8)
        {
        	entityplayer.epm.setSprinting(false);
        }
        if(entityplayer.epm.getHealth() == 0)
        {
        	//mod_ThirstMod.makeDefault();
        	//Used to fix bug, of stats not reseting on death.
        }
    }

    /*public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        if(nbttagcompound.hasKey("thirstLevel"))
        {
            thirstLevel = nbttagcompound.getInteger("thirstLevel");
            thirstTickTimer = nbttagcompound.getInteger("thirstTickTimer");
            thirstSaturationLevel = nbttagcompound.getFloat("thirstSaturationLevel");
            thirstExhaustionLevel = nbttagcompound.getFloat("thirstExhaustionLevel");
            isThirstPoisoned = nbttagcompound.getBoolean("isThirstPoisoned");
            thirstDuration = nbttagcompound.getInteger("thirstPoisonCountdown");
            //thirstHang = nbttagcompound.getInteger("thirstHang");
        }
        System.out.println("Read!");
    }*/

    /*Moved to mod_ThirstMod  because unsure whether java can pass by reference
     * public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setInteger("thirstLevel", thirstLevel);
        nbttagcompound.setInteger("thirstTickTimer", thirstTickTimer);
        nbttagcompound.setFloat("thirstSaturationLevel", thirstSaturationLevel);
        nbttagcompound.setFloat("thirstExhaustionLevel", thirstExhaustionLevel);
        System.out.println("Written!");
    }*/

    public int getThirstLevel()
    {
        return thirstLevel;
    }

    public int getPrevThirstLevel()
    {
        return prevthirstLevel;
    }

    public boolean needWater()
    {
        return thirstLevel < 20;
    }

    public void addExhaustion(float f)
    {
        thirstExhaustionLevel = Math.min(thirstExhaustionLevel + f, 40F);
    }

    public float getThirstSaturationLevel()
    {
        return thirstSaturationLevel1;
    }

    public void setThirstLevel(int i)
    {
        thirstLevel = i;
    }

    public void setThirstSaturationLevel(float f)
    {
        thirstSaturationLevel1 = f;
    }
    
    //remove when doing api
    public void addThirstStatsFrom(Drink itemdrink)
    {
        addStats(itemdrink.getHealAmount(), itemdrink.getThirstSaturationModifier());
    }
    
    public void addThirstStatsFrom(ItemWoodDrink itemdrink)
    {
        addStats(itemdrink.getHealAmount(), itemdrink.getThirstSaturationModifier());
    }
}
