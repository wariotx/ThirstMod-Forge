package net.minecraft.src.thirstmod.itemmod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.thirstmod.Utilities;

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
        Utilities.getStats().addStats(20, 1);
        return new ItemStack(Item.bowlEmpty);
    }
}
