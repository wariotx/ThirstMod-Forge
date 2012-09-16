package tarun1998.thirstmod.blocks;

import net.minecraft.src.*;

public class ContainerRC extends Container {
	private TileEntityRC rc;
	private int lastRainMeter = 0;
	private int lastInternalBucket = 0;
	
	public ContainerRC(InventoryPlayer ip, TileEntityRC tile) {
		rc = tile;
		addSlotToContainer(new Slot(tile, 0, 56, 53));
		addSlotToContainer(new SlotFurnace(ip.player, tile, 1, 116, 35));
		
		int var3;
		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				addSlotToContainer(new Slot(ip, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			addSlotToContainer(new Slot(ip, var3, 8 + var3 * 18, 142));
		}
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
	public void updateCraftingResults() {
		super.updateCraftingResults();
		
		for (int var1 = 0; var1 < crafters.size(); ++var1) {
			ICrafting var2 = (ICrafting) crafters.get(var1);
			if (lastRainMeter != rc.RainMeter) {
				var2.updateCraftingInventoryInfo(this, 0, rc.RainMeter);
			}

			if (lastInternalBucket != rc.internalBucket) {
				var2.updateCraftingInventoryInfo(this, 1, rc.internalBucket);
			}
		}

		lastRainMeter = rc.RainMeter;
		lastInternalBucket = rc.internalBucket;
	}
	
	@Override
	public void updateProgressBar(int i, int j) {
		super.updateProgressBar(i, j);
		switch (i) {
		case 0:
			rc.RainMeter = j;
			return;
		case 1:
			rc.internalBucket = j;
			return;
		case 2:
			rc.isActive = j == 1;
		default:
		}

	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.updateCraftingInventoryInfo(this, 0, this.rc.RainMeter);
        par1ICrafting.updateCraftingInventoryInfo(this, 1, this.rc.internalBucket);
    }

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return rc.isUseableByPlayer(entityplayer);
	}
}
