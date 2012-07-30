package net.minecraft.src.thirstmod;

import java.util.*;

import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.*;

public class ItemDrink extends Item implements ITextureProvider {
	private final int healAmount;
	private final float saturationThirstModifier;
	private boolean alwaysItemDrinkable;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;
	private boolean hasEffect = false;
	private Random rand = new Random();

	private float poisonChance;

	private int foodHeal;
	private float satHeal;

	public ItemDrink(int id, int thirstReplenish, float saturationModifier, boolean alwaysItemDrinkable) {
		super(id);
		healAmount = thirstReplenish;
		saturationThirstModifier = saturationModifier;
		setMaxStackSize(ConfigHelper.maxStackSize);
		if (alwaysItemDrinkable == true) {
			this.alwaysItemDrinkable = true;
		}
	}

	public ItemDrink(int i, int j, boolean flag) {
		this(i, j, 0.2F, flag);
	}

	public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		itemstack.stackSize--;
		getStats().addStats(healAmount, saturationThirstModifier);
		if (!world.isRemote && potionId > 0 && world.rand.nextFloat() < potionEffectProbability) {
			entityplayer.addPotionEffect(new PotionEffect(potionId,
					potionDuration * 20, potionAmplifier));
		}
		if (poisonChance > 0) {
			if(rand.nextInt() < poisonChance && ConfigHelper.poisonOn == true) {
				getStats().poisoned = true;
			}
		}
		if (foodHeal > 0 && satHeal > 0) {
			entityplayer.getFoodStats().addStats(foodHeal, satHeal);
		}
		if (itemstack.stackSize <= 0) {
			return new ItemStack(Item.glassBottle);
		} else {
			entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
			return itemstack;
		}
	}

	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack itemstack) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		return itemstack;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public float getThirstSaturationModifier() {
		return saturationThirstModifier;
	}

	public ItemDrink setPotionEffect(int i, int j, int k, float f) {
		potionId = i;
		potionDuration = j;
		potionAmplifier = k;
		potionEffectProbability = f;
		return this;
	}

	public boolean hasEffect(ItemStack itemstack) {
		if (hasEffect == true) {
			return true;
		} else {
			return false;
		}
	}

	public ItemDrink setPoisoningChance(float chance) {
		poisonChance = chance;
		return this;
	}

	public ThirstStats getStats() {
		return Utilities.getStats();
	}

	public Item setHasEffect() {
		hasEffect = true;
		return this;
	}

	public ItemDrink setEffect(boolean b) {
		if (b == true) {
			hasEffect = true;
		} else if (b == false) {
		}
		return this;
	}

	public ItemDrink healFood(int level, float saturation) {
		foodHeal = level;
		satHeal = saturation;
		return this;
	}

	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}