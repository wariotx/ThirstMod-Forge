package tarun1998.thirstmod.blocks;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.SlotFurnace;

public class ContainerFilter extends Container{
	private TileEntityFilter tile;
	private int lastFuelTime;
	private int lastMakeTime;
	private int lastID;
	
	public ContainerFilter(InventoryPlayer inv, TileEntityFilter tile) {
		this.tile = tile;
		this.addSlotToContainer(new Slot(tile, 0, 59, 14));
		this.addSlotToContainer(new Slot(tile, 1, 59, 56));
		this.addSlotToContainer(new Slot(tile, 2, 38, 35));
		this.addSlotToContainer(new SlotFurnace(inv.player, tile, 3, 116, 35));
		
		int var3;
		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(inv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			this.addSlotToContainer(new Slot(inv, var3, 8 + var3 * 18, 142));
		}
	}
	
	@Override
	public void updateCraftingResults() {
		for (int var1 = 0; var1 < crafters.size(); ++var1) { 
			ICrafting crafting = (ICrafting) crafters.get(var1);
			if(lastFuelTime != tile.fuelTime) {
				crafting.updateCraftingInventoryInfo(this, 0, tile.fuelTime);
			}
			if(lastMakeTime != tile.makeTime) {
				crafting.updateCraftingInventoryInfo(this, 1, tile.makeTime);
			}
			if(lastID != tile.startingID) {
				crafting.updateCraftingInventoryInfo(this, 2, tile.startingID);
			}
		}
		lastFuelTime = tile.fuelTime;
		lastMakeTime = tile.makeTime;
		lastID = tile.startingID;
	}
	
	@Override
	public void updateProgressBar(int par1, int par2) {
		switch(par1) {
			case 0: 
				tile.fuelTime = par2;
				return;
			case 1:
				tile.makeTime = par2;
				return;
			case 2:
				tile.startingID = par2;
			default:
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1iCrafting) {
		par1iCrafting.updateCraftingInventoryInfo(this, 0, tile.fuelTime);
		par1iCrafting.updateCraftingInventoryInfo(this, 1, tile.makeTime);
		par1iCrafting.updateCraftingInventoryInfo(this, 2, tile.startingID);
	}
	
	@Override
	public ItemStack transferStackInSlot(int par1) {
		ItemStack var2 = null;
		Slot var3 = (Slot) inventorySlots.get(par1);
		if (var3 != null && var3.getHasStack()) {
			ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			if (par1 < 2) {
				mergeItemStack(var4, 2, 38, false);
			} else if (par1 >= 2 && par1 < 29) {
				mergeItemStack(var4, 0, 1, false);
			} else if (par1 >= 29 && par1 < 38) {
				mergeItemStack(var4, 2, 29, false);
			}

			if (var4.stackSize == 0) {
				var3.putStack((ItemStack) null);
			} else {
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize) {
				return null;
			}
			var3.putStack(var4);
		}
		return var2;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}
}
