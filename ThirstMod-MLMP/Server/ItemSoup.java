package net.minecraft.src;

import net.minecraft.src.ThirstMod.other.PlayerThirst;

public class ItemSoup extends ItemFood
{
    public ItemSoup(int par1, int par2)
    {
        super(par1, par2, false);
        setMaxStackSize(1);
    }

    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        PlayerThirst.getStats().addStats(10, 5.2f);
        return new ItemStack(Item.bowlEmpty);
    }
}
