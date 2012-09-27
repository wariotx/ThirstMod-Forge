package tarun1998.thirstmod.reflection;

import tarun1998.thirstmod.ThirstUtils;
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
    	if(!par2World.isRemote) {
    		ThirstUtils.getStats().addStats(10, 3f);
    	}
        return new ItemStack(Item.bowlEmpty);
    }
}
