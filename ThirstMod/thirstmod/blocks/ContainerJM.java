package net.minecraft.src.thirstmod.blocks;

import net.minecraft.src.*;
import net.minecraft.src.thirstmod.ThirstUtils;

public class ContainerJM extends Container {
	private TileEntityJM furnace;
	private int lastCookTime = 0;
	private int lastBurnTime = 0;
	private int lastItemBurnTime = 0;
	
	public ContainerJM(InventoryPlayer par1InventoryPlayer, TileEntityJM par2TileEntityFurnace) {
		this.furnace = par2TileEntityFurnace;
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 58, 24));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 30, 24));
		this.addSlotToContainer(new Slot(par2TileEntityFurnace, 3, 44, 47));
		this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 2, 116, 35));
		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
		}
	}
	
	@Override
	public void updateCraftingResults() {
		super.updateCraftingResults();

		for (int var1 = 0; var1 < this.crafters.size(); ++var1) {
			ICrafting var2 = (ICrafting) this.crafters.get(var1);

			if (this.lastCookTime != this.furnace.brewrCoolTime) {
				var2.updateCraftingInventoryInfo(this, 0, this.furnace.brewrCoolTime);
			}

			if (this.lastBurnTime != this.furnace.brewrbrewTime) {
				var2.updateCraftingInventoryInfo(this, 1, this.furnace.brewrbrewTime);
			}

			if (this.lastItemBurnTime != this.furnace.currentItemCoolTime) {
				var2.updateCraftingInventoryInfo(this, 2, this.furnace.currentItemCoolTime);
			}
		}

		this.lastCookTime = this.furnace.brewrCoolTime;
		this.lastBurnTime = this.furnace.brewrbrewTime;
		this.lastItemBurnTime = this.furnace.currentItemCoolTime;
	}
	
	@Override
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.furnace.brewrCoolTime = par2;
		}

		if (par1 == 1) {
			this.furnace.brewrbrewTime = par2;
		}

		if (par1 == 2) {
			this.furnace.currentItemCoolTime = par2;
		}
	}

	@Override
	public ItemStack transferStackInSlot(int par1) {
		ItemStack var2 = null;
		Slot var3 = (Slot) this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();

			if (par1 == 2) {
				if (!this.mergeItemStack(var4, 4, 39, true)) {
					return null;
				}

				var3.onSlotChange(var4, var2);
			} else if (par1 >= 4 && par1 < 30) {
				if (!this.mergeItemStack(var4, 30, 39, false)) {
					return null;
				}
			} else if (par1 >= 30 && par1 < 39) {
				if (!this.mergeItemStack(var4, 3, 30, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(var4, 4, 39, false)) {
				return null;
			}

			if (var4.stackSize == 0) {
				var3.putStack((ItemStack) null);
			} else {
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize) {
				return null;
			}

			var3.onPickupFromSlot(var4);
		}

		return var2;
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.updateCraftingInventoryInfo(this, 0, this.furnace.brewrbrewTime);
        par1ICrafting.updateCraftingInventoryInfo(this, 1, this.furnace.brewrCoolTime);
        par1ICrafting.updateCraftingInventoryInfo(this, 2, this.furnace.currentItemCoolTime);
        par1ICrafting.updateCraftingInventoryInfo(this, 3, this.furnace.makeTime);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.furnace.isUseableByPlayer(entityplayer);
	}
}
