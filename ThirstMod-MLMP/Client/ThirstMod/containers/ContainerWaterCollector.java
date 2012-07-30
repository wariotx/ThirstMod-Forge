package net.minecraft.src.ThirstMod.containers;

import net.minecraft.src.*;

public class ContainerWaterCollector extends Container
{
    private TileEntityWaterCollector rc;
    private int lastRainMeter = 0;
    private int lastInternalBucket = 0;

    public ContainerWaterCollector(InventoryPlayer var1, TileEntityWaterCollector var2)
    {
        this.rc = var2;
        this.addSlot(new Slot(var2, 0, 56, 53));
        this.addSlot(new SlotFurnace(var1.player, var2, 1, 116, 35));

        int var3;
        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(var1, var3, 8 + var3 * 18, 142));
        }
    }

    public TileEntityWaterCollector getTileEntityRC()
    {
        return this.rc;
    }

    public ItemStack transferStackInSlot(int var1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(var1);
        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();
            if (var1 < 2)
            {
                this.mergeItemStack(var4, 2, 38, false);
            }
            else if (var1 >= 2 && var1 < 29)
            {
                this.mergeItemStack(var4, 0, 1, false);
            }
            else if (var1 >= 29 && var1 < 38)
            {
                this.mergeItemStack(var4, 2, 29, false);
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.putStack(var4);
        }

        return var2;
    }

    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);
            if (this.lastRainMeter != this.rc.RainMeter)
            {
                var2.updateCraftingInventoryInfo(this, 0, this.rc.RainMeter);
            }

            if (this.lastInternalBucket != this.rc.internalBucket)
            {
                var2.updateCraftingInventoryInfo(this, 1, this.rc.internalBucket);
            }
        }

        this.lastRainMeter = this.rc.RainMeter;
        this.lastInternalBucket = this.rc.internalBucket;
    }

    public void updateProgressBar(int var1, int var2)
    {
        switch (var1)
        {
            case 0:
                this.rc.RainMeter = var2;
                return;
            case 1:
                this.rc.internalBucket = var2;
                return;
            case 2:
                this.rc.isActive = var2 == 1;
            default:
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return rc.isUseableByPlayer(entityplayer);
	}
}
