package net.minecraft.src.ThirstMod.containers;

import net.minecraft.src.*;

public class ContainerJM extends Container
{
    private TileEntityJM furnace;
    private int lastCookTime = 0;
    private int lastBurnTime = 0;
    private int lastItemBurnTime = 0;

    public ContainerJM(InventoryPlayer par1InventoryPlayer, TileEntityJM par2TileEntityFurnace)
    {
        this.furnace = par2TileEntityFurnace;
        this.addSlot(new Slot(par2TileEntityFurnace, 0, 58, 24));
        this.addSlot(new Slot(par2TileEntityFurnace, 1, 30, 24));
        this.addSlot(new Slot(par2TileEntityFurnace, 3, 44, 47));
        this.addSlot(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 2, 116, 35));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
        }
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        for (int var1 = 0; var1 < this.crafters.size(); ++var1)
        {
            ICrafting var2 = (ICrafting)this.crafters.get(var1);

            if (this.lastCookTime != this.furnace.freezerCoolTime)
            {
                var2.updateCraftingInventoryInfo(this, 0, this.furnace.freezerCoolTime);
            }

            if (this.lastBurnTime != this.furnace.freezerFreezeTime)
            {
                var2.updateCraftingInventoryInfo(this, 1, this.furnace.freezerFreezeTime);
            }

            if (this.lastItemBurnTime != this.furnace.currentItemCoolTime)
            {
                var2.updateCraftingInventoryInfo(this, 2, this.furnace.currentItemCoolTime);
            }
        }

        this.lastCookTime = this.furnace.freezerCoolTime;
        this.lastBurnTime = this.furnace.freezerFreezeTime;
        this.lastItemBurnTime = this.furnace.currentItemCoolTime;
    }

    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.furnace.freezerCoolTime = par2;
        }

        if (par1 == 1)
        {
            this.furnace.freezerFreezeTime = par2;
        }

        if (par1 == 2)
        {
            this.furnace.currentItemCoolTime = par2;
        }
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.furnace.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 2)
            {
                if (!this.mergeItemStack(var4, 4, 39, true))
                {
                    return null;
                }

                var3.func_48417_a(var4, var2);
            }
            else if (par1 >= 4 && par1 < 30)
            {
                if (!this.mergeItemStack(var4, 30, 39, false))
                {
                    return null;
                }
            }
            else if (par1 >= 30 && par1 < 39)
            {
                if (!this.mergeItemStack(var4, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 4, 39, false))
            {
                return null;
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

            var3.onPickupFromSlot(var4);
        }

        return var2;
    }
}
