/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.containers;

import net.minecraft.src.*;

public class SlotJM extends Slot {
	public SlotJM(EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k) {
		super(iinventory, i, j, k);
	}

	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	public void onPickupFromSlot(ItemStack itemstack) {
		super.onPickupFromSlot(itemstack);
	}
}
