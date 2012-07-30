package net.minecraft.src.ThirstMod.containers;

import net.minecraft.src.*;

public class TileEntityJM extends TileEntity implements IInventory
{
    private ItemStack stacks[];
    public int freezerFreezeTime;
    public int currentItemCoolTime;
    public int freezerCoolTime;
    public int makeTime;

    public TileEntityJM()
    {
        stacks = new ItemStack[4];
        freezerFreezeTime = 0;
        currentItemCoolTime = 0;
        freezerCoolTime = 0;
        makeTime = 0;
    }

    public int getSizeInventory()
    {
        return stacks.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return stacks[i];
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if (stacks[i] != null)
        {
            if (stacks[i].stackSize <= j)
            {
                ItemStack itemstack = stacks[i];
                stacks[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = stacks[i].splitStack(j);
            if (stacks[i].stackSize == 0)
            {
                stacks[i] = null;
            }
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        stacks[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    public String getInvName()
    {
        return "Juice Maker";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        stacks = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("SlotFreezer");
            if (byte0 >= 0 && byte0 < stacks.length)
            {
                stacks[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        freezerFreezeTime = nbttagcompound.getShort("FreezeTime");
        freezerCoolTime = nbttagcompound.getShort("CoolTime");
        currentItemCoolTime = getItemFreezeTime(stacks[1]);
        makeTime = nbttagcompound.getShort("SomeTime");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("FreezeTime", (short)freezerFreezeTime);
        nbttagcompound.setShort("CoolTime", (short)freezerCoolTime);
        nbttagcompound.setShort("SomeTime", (short)makeTime);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < stacks.length; i++)
        {
            if (stacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("SlotFreezer", (byte)i);
                stacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public int getCoolProgressScaled(int i)
    {
        return (freezerCoolTime * i) / 200;
    }

    public int getFreezeTimeRemainingScaled(int i)
    {
        if (currentItemCoolTime == 0)
        {
            currentItemCoolTime = 200;
        }
        return (makeTime * i) / currentItemCoolTime;
    }

    public boolean isFreezing()
    {
        return freezerFreezeTime > 0;
    }

    public void updateEntity()
    {
        boolean flag = freezerFreezeTime > 0;
        boolean flag1 = false;
        if (freezerFreezeTime > 0)
        {
            freezerFreezeTime--;
        }
        if(makeTime > 0)
        {
        	makeTime--;
        }
        if (!worldObj.isRemote)
        {
        	if(makeTime == 0 && canSolidify())
            {
            	makeTime = getItemMake(stacks[3]);
            	if(makeTime > 0)
                {
                	if (stacks[3] != null)
                    {
                        if (stacks[3].getItem().hasContainerItem())
                        {
                            stacks[3] = new ItemStack(stacks[3].getItem().getContainerItem());
                        }
                        else
                        {
                            stacks[3].stackSize--;
                        }
                        if (stacks[3].stackSize == 0)
                        {
                            stacks[3] = null;
                        }
                    }
                }
            }
            if (freezerFreezeTime == 0 && makeTime > 0 && canSolidify())
            {
                currentItemCoolTime = freezerFreezeTime = getItemFreezeTime(stacks[1]);
                if (freezerFreezeTime > 0)
                {
                    flag1 = true;
                    if (stacks[1] != null)
                    {
                        if (stacks[1].getItem().hasContainerItem())
                        {
                            stacks[1] = new ItemStack(stacks[1].getItem().getContainerItem());
                        }
                        else
                        {
                            stacks[1].stackSize--;
                        }
                        if (stacks[1].stackSize == 0)
                        {
                            stacks[1] = null;
                        }
                    }
                }
            }
            if (isFreezing() && canSolidify())
            {
                freezerCoolTime++;
                if (freezerCoolTime == 200)
                {
                    freezerCoolTime = 0;
                    solidifyItem();
                    flag1 = true;
                }
            }
            else
            {
                freezerCoolTime = 0;
            }
        }
        if (flag != (freezerFreezeTime > 0))
        {
            flag1 = true;
        }
        if (flag1)
        {
            onInventoryChanged();
        }
    }

    private boolean canSolidify()
    {
    	if (this.stacks[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack var1 = JMRecipes.solidifying().getSmeltingResult(this.stacks[0]);
            if (var1 == null) return false;
            if (this.stacks[2] == null) return true;
            if (!this.stacks[2].isItemEqual(var1)) return false;
            int result = stacks[2].stackSize + var1.stackSize;
            return (result <= getInventoryStackLimit() && result <= var1.getMaxStackSize());
        }
    }

    public void solidifyItem()
    {
    	if (this.canSolidify())
        {
            ItemStack var1 = JMRecipes.solidifying().getSmeltingResult(stacks[0]);

            if (this.stacks[2] == null)
            {
                this.stacks[2] = var1.copy();
            }
            else if (this.stacks[2].isItemEqual(var1))
            {
                ++this.stacks[2].stackSize;
            }

            --this.stacks[0].stackSize;

            if (this.stacks[0].stackSize <= 0)
            {
                this.stacks[0] = null;
            }
        }
    }

    public static int getItemFreezeTime(ItemStack itemstack)
    {
    	int fail = 0;
        if (itemstack == null)
        {
            return 0;
        }
        int i = itemstack.getItem().shiftedIndex;
        if(i == Item.glassBottle.shiftedIndex)
        {
        	return 200;
        }
        else
        {
        	return fail;
        }
    }
    
    public static int getItemMake(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return 0;
        }
        int i = itemstack.getItem().shiftedIndex;
        if (i < 256 && Block.blocksList[i].blockMaterial == Material.wood)
        {
            return 300;
        }

        if (i == Item.stick.shiftedIndex)
        {
            return 100;
        }

        if (i == Item.coal.shiftedIndex)
        {
            return 1600;
        }

        if (i == Item.bucketLava.shiftedIndex)
        {
            return 20000;
        }

        if (i == Block.sapling.blockID)
        {
            return 100;
        }

        if (i == Item.blazeRod.shiftedIndex)
        {
            return 2400;
        }
        else
        {
            return ModLoader.addAllFuel(itemstack.itemID, itemstack.getItemDamage());
        }
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        else
        {
            return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
        }
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) 
	{

        if (this.stacks[var1] != null)
        {
            ItemStack var2 = this.stacks[var1];
            this.stacks[var1] = null;
            return var2;
        }
        else
        {
            return null;
        }
	}
}
