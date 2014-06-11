package tarun1998.thirstmod.blocks;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.src.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityRC extends TileEntity implements IInventory, ISidedInventory {
	public ItemStack rainItemStacks[];
	public int RainMeter;
	public int internalBucket;
	public static boolean isActive;

	public TileEntityRC() {
		rainItemStacks = new ItemStack[2];
		RainMeter = 0;
		internalBucket = 0;
	}

	@Override
	public void updateEntity() {
		if (worldObj != null) {
			try {
				boolean flag = (Boolean) ObfuscationReflectionHelper.getPrivateValue(WorldInfo.class, worldObj.getWorldInfo(), 12);
				boolean flag1 = canRainOn(xCoord, yCoord, zCoord, worldObj);
				isActive = flag && flag1;
				if (isActive == true && canFill()) {
					RainMeter++;
					if (RainMeter == RCRecipes.fill().getFillTimeFor(rainItemStacks[0].itemID)) {
						RainMeter = 0;
						fillItem();
					}
				} else if (isActive && !canFill()) {
					if (internalBucket < 2000) {
						internalBucket++;
					}
					RainMeter = 0;
				} else if (!isActive && canFill() && internalBucket > 0) {
					RainMeter++;
					internalBucket--;
					if (RainMeter == RCRecipes.fill().getFillTimeFor(rainItemStacks[0].itemID)) {
						RainMeter = 0;
						fillItem();
					}
				} else if (!canFill()) {
					RainMeter = 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return rainItemStacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (rainItemStacks[i] != null) {
			if (rainItemStacks[i].stackSize <= j) {
				ItemStack itemstack = rainItemStacks[i];
				rainItemStacks[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = rainItemStacks[i].splitStack(j);
			if (rainItemStacks[i].stackSize == 0) {
				rainItemStacks[i] = null;
			}
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.rainItemStacks[i] != null) {
			ItemStack var2 = this.rainItemStacks[i];
			this.rainItemStacks[i] = null;
			return var2;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		rainItemStacks[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName() {
		return "Rain Collector";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		} else {
			return entityplayer.getDistanceSq((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
		}
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		rainItemStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if (byte0 >= 0 && byte0 < rainItemStacks.length) {
				rainItemStacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}

		RainMeter = nbttagcompound.getShort("RainMeter");
		internalBucket = nbttagcompound.getShort("InternalBucket");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setShort("RainMeter", (short) RainMeter);
		nbttagcompound.setShort("InternalBucket", (short) internalBucket);
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < rainItemStacks.length; i++) {
			if (rainItemStacks[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				rainItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("Items", nbttaglist);
	}

	public void fillItem() {
		if (!canFill()) {
			return;
		}

		ItemStack itemstack = RCRecipes.fill().getSolidifyingResult(rainItemStacks[0].getItem().shiftedIndex);

		if (rainItemStacks[1] == null) {
			rainItemStacks[1] = itemstack.copy();
		} else if (rainItemStacks[1].itemID == itemstack.itemID) {
			rainItemStacks[1].stackSize += itemstack.stackSize;
		}

		if (rainItemStacks[0].getItem().isMap()) {
			rainItemStacks[0] = new ItemStack(rainItemStacks[0].getItem().setFull3D());
		} else {
			rainItemStacks[0].stackSize--;
		}

		if (rainItemStacks[0].stackSize <= 0) {
			rainItemStacks[0] = null;
		}
	}

	private boolean canFill() {
		if (rainItemStacks[0] == null) {
			return false;
		}

		ItemStack itemstack = RCRecipes.fill().getSolidifyingResult(rainItemStacks[0].getItem().shiftedIndex);

		if (itemstack == null) {
			return false;
		}

		if (rainItemStacks[1] == null) {
			return true;
		}

		if (!rainItemStacks[1].isItemEqual(itemstack)) {
			return false;
		}

		if (rainItemStacks[1].stackSize < getInventoryStackLimit() && rainItemStacks[1].stackSize < rainItemStacks[1].getMaxStackSize()) {
			return true;
		}

		return rainItemStacks[1].stackSize < itemstack.getMaxStackSize();
	}

	public boolean canRainOn(int i, int j, int k, World world) {
		for (int l = j + 1; l < 255; l++) {
			if (world.getBlockId(i, l, k) != 0) {
				return false;
			}
		}
		return true;
	}

	public int getRainMeterScaled(int i) {
		int fillTime;
		if(rainItemStacks[0] != null) {
			fillTime = RCRecipes.fill().getFillTimeFor(rainItemStacks[0].itemID);
		} else fillTime = 200;
		return (RainMeter * i) / fillTime;
	}

	public int getInternalBucketScaled(int i) {
		return (internalBucket * i) / 2000;
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if (side == ForgeDirection.DOWN) return 1;
		if (side == ForgeDirection.UP) return 0;
		return 2;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}
}
