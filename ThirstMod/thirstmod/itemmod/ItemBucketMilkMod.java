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

import net.minecraft.src.*;
import net.minecraft.src.thirstmod.core.*;

public class ItemBucketMilkMod extends Item
{
    public ItemBucketMilkMod(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
    }

    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        --par1ItemStack.stackSize;
        
        Utilities.getStats().addStats(10, 5f);

        if (!par2World.isRemote)
        {
            par3EntityPlayer.clearActivePotions();
        }

        return par1ItemStack.stackSize <= 0 ? new ItemStack(Item.bucketEmpty) : par1ItemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }
}
