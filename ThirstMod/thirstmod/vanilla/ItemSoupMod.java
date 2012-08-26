package net.minecraft.src.thirstmod.vanilla;

import net.minecraft.src.thirstmod.ThirstUtils;
import net.minecraft.src.*;

public class ItemSoupMod extends ItemFood
{
    public ItemSoupMod(int par1, int par2)
    {
        super(par1, par2, false);
        this.setMaxStackSize(1);
    }

    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        ThirstUtils.getStats().addStats(20, 1);
        return new ItemStack(Item.bowlEmpty);
    }
}
