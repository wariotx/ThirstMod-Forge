package tarun1998.thirstmod.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.src.*;

public class TileEntityFilter extends TileEntity implements IInventory {
    // 0 = input, 1 = filter block, 2 = fuel source, 3 = output
    public ItemStack filterStacks[] = new ItemStack[4];
    public int fuelTime;
    public int makeTime = 600; //30 seconds.
    public int startingID;

    @Override
    public void updateEntity() {
        System.out.println(makeTime);
        if (!worldObj.isRemote) {
            if (filterStacks[0] != null && filterStacks[1] != null && filterStacks[3] == null) {
                if (getFuelTime(filterStacks[2]) > 0) {
                    fuelTime = getFuelTime(filterStacks[2]);
                    --filterStacks[2].stackSize;
                    if (filterStacks[2].stackSize <= 0) {
                        filterStacks[2] = null;
                    }
                } else if (fuelTime > 0 && FilterRecipes.getInstance().getRecipes().get(filterStacks[0].itemID) != null) {
                    fuelTime--;
                    if (filterStacks[1].itemID == Block.cloth.blockID && filterStacks[1].getItemDamage() == 0) {
                        startingID = filterStacks[1].itemID;
                    }
                    if (startingID == Block.cloth.blockID) {
                        makeTime--;
                        if (makeTime == 400) {
                            changeWoolColour(8);
                            System.out.println("Set wool color1");
                        } else if (makeTime == 200) {
                            changeWoolColour(7);
                            System.out.println("Set wool color2");
                        } else if (makeTime == 0) {
                            filterItem(filterStacks[0].getItem().shiftedIndex);
                            System.out.println("Filtered Item");
                        }
                    }
                }
            }
        }
    }

    public void changeWoolColour(int meta) {
        ItemStack stack = new ItemStack(Block.cloth, filterStacks[1].stackSize, meta);
        filterStacks[1] = stack.copy();
    }

    public void filterItem(int itemID) {
        ItemStack var1 = FilterRecipes.getInstance().getRecipes().get(itemID);
        if (this.filterStacks[3] == null) {
            this.filterStacks[3] = var1.copy();
        } else if (this.filterStacks[3].isItemEqual(var1)) {
            ++this.filterStacks[3].stackSize;
        }

        --this.filterStacks[0].stackSize;

        if (this.filterStacks[0].stackSize <= 0) {
            this.filterStacks[0] = null;
        }
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return filterStacks[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (filterStacks[i] != null) {
            if (filterStacks[i].stackSize <= j) {
                ItemStack itemstack = filterStacks[i];
                filterStacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = filterStacks[i].splitStack(j);
            if (filterStacks[i].stackSize == 0) {
                filterStacks[i] = null;
            }
            return itemstack1;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        if (filterStacks[i] != null) {
            ItemStack var2 = filterStacks[i];
            filterStacks[i] = null;
            return var2;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {
        filterStacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public int getFuelTime(ItemStack stack) {
        int i;
        if (stack != null) {
            i = stack.itemID;
        } else i = 0;

        if (i == Item.stick.shiftedIndex) {
            return 100;
        }

        if (i == Item.coal.shiftedIndex) {
            return 1600;
        }

        if (i == Item.bucketLava.shiftedIndex) {
            return 20000;
        }

        if (i == Block.sapling.blockID) {
            return 100;
        }

        if (i == Item.blazeRod.shiftedIndex) {
            return 2400;
        } else {
            return GameRegistry.getFuelValue(stack);
        }
    }

    @Override
    public String getInvName() {
        return "Filter";
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }
}
