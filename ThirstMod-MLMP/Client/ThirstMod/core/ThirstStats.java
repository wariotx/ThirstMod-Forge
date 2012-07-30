package net.minecraft.src.ThirstMod.core;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_ThirstMod;
import net.minecraft.src.ThirstMod.*;
import net.minecraft.src.ThirstMod.drinks.Drink;
import net.minecraft.src.ThirstMod.drinks.ItemWoodDrink;

public class ThirstStats 
{
	public static int thirstLevel;
    public static float thirstSaturationLevel1;
    public float thirstExhaustionLevel = 0;
    public int thirstTickTimer;
    public int prevthirstLevel;
    public boolean isThirstPoisoned;
    public int thirstDuration;
    public int thirstHang;

    public ThirstStats()
    {
        thirstTickTimer = 0;
        thirstLevel = 20;
        prevthirstLevel = 20;
        thirstSaturationLevel1 = 5F;
        isThirstPoisoned = false;
        thirstDuration = 360;
        thirstHang = 0;
    }

    public static void addStats(int i, float f)
    {
        thirstLevel = Math.min(i + thirstLevel, 20);
        thirstSaturationLevel1 = Math.min(thirstSaturationLevel1 + (float)i * f * 2.0F, thirstLevel);
    }

    public void onUpdate(EntityPlayer entityplayer)
    {
        int i;
        //remove below when doing api
        if(mod_ThirstMod.thirstInPeaceful == true)
        {
        	i = 1;
        }
        else
        {
        	i = entityplayer.worldObj.difficultySetting;
        }
        prevthirstLevel = thirstLevel;
        //thirstHang++;
        if(thirstExhaustionLevel > 4F)
        {
            thirstExhaustionLevel -= 4F;
            if(thirstSaturationLevel1 > 0.0F)
            {
                thirstSaturationLevel1 = Math.max(thirstSaturationLevel1 - 1.0F, 0.0F);
            } 
            if(i > 0 && thirstSaturationLevel1 == 0.0)
            {
                thirstLevel = Math.max(thirstLevel - 1, 0);
            }
        }
        if(!entityplayer.isSprinting() || thirstLevel<20)
        {
        	mod_ThirstMod.setSpeedOnGround(0.1f);
    		mod_ThirstMod.setSpeedInAir(0.02f);
        }
        if(thirstLevel >= 18 && i>0)
        {
        	if(thirstLevel ==20 && entityplayer.isSprinting())
        	{
        		mod_ThirstMod.setSpeedOnGround(0.15f);
        		mod_ThirstMod.setSpeedInAir(0.03f);
        	}
        	if(entityplayer.shouldHeal())
        	{
        		thirstTickTimer++;
        		if(thirstTickTimer >= 80)
        		{
        			entityplayer.heal(1);
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
                if(entityplayer.getHealth() > 10 || i >= 3 || entityplayer.getHealth() > 1 && i >= 2)
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
        	entityplayer.setSprinting(false);
        }
        if(entityplayer.getHealth() == 0)
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
