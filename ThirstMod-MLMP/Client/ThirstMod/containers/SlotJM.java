package net.minecraft.src.ThirstMod.containers;

import net.minecraft.src.*;

public class SlotJM extends Slot
{
    public SlotJM(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        super.onPickupFromSlot(itemstack);
    }
}
