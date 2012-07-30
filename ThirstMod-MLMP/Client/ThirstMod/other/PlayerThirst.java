package net.minecraft.src.ThirstMod.other;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.core.*;

public class PlayerThirst extends PlayerBase
{
	private boolean addExhaustion = false;
	private Random random = new Random();
	private boolean gameInitialised = false;
	private int attempts = 0;
	public static int duration = 0;
	public static int time = 360;
	
	public static List biomes = new ArrayList();
	public static float poisonRate;
	
	public PlayerThirst(PlayerAPI playerapi)
	{
		super(playerapi);
	}
	
	public void onUpdate()
	{
		super.onUpdate();
	}
		
	public void attackTargetEntityWithCurrentItem(Entity entity)
	{
		super.attackTargetEntityWithCurrentItem(entity);
		ThirstUtils.addExhaustion(0.6f);
	}
	
	public void damageEntity(DamageSource damagesource, int i)
	{
		super.damageEntity(damagesource, i);
		ThirstUtils.addExhaustion(0.6f);
	}
	
	public boolean canHarvestBlock(Block block)
	{
		if(block == Block.obsidian)
		{
			ThirstUtils.addExhaustion(0.1f);
			//I don't know why i added this.
		}
		ThirstUtils.addExhaustion(0.034f);
		return super.canHarvestBlock(block);
	}
}