package net.minecraft.src.ThirstMod.drinks;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraft.src.mod_ThirstMod;

public class ItemWoodBottle extends Item
{
    public ItemWoodBottle(int par1)
    {
        super(par1);
    }
    
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	return super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

        if (movingobjectposition == null)
        {
            return par1ItemStack;
        }

        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;

            if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
            {
                return par1ItemStack;
            }

            if (!par3EntityPlayer.canPlayerEdit(i, j, k))
            {
                return par1ItemStack;
            }

            if (par2World.getBlockMaterial(i, j, k) == Material.water)
            {
                par1ItemStack.stackSize--;

                if (par1ItemStack.stackSize <= 0)
                {
                	return new ItemStack(mod_ThirstMod.waterWood);
                }
            }
        }

        return par1ItemStack;
    }
}
