/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.itemmod;

import net.minecraft.src.thirstmod.core.*;
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
        Utilities.getStats().addStats(20, 1);
        return new ItemStack(Item.bowlEmpty);
    }
}
