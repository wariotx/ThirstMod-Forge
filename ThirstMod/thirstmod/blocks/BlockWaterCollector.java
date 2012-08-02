/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.thirstmod.containers.TileEntityWaterCollector;
import net.minecraft.src.thirstmod.guis.GuiWaterCollector;
import net.minecraft.src.*;

public class BlockWaterCollector extends BlockContainer implements ITextureProvider {
	private Random rand = new Random();
	private final boolean isActive;

	public BlockWaterCollector(int var1, boolean flag) {
		super(var1, Material.rock);
		isActive = flag;
		blockIndexInTexture = 3;
	}

	public TileEntity getBlockEntity() {
		return new TileEntityWaterCollector();
	}

	public boolean blockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5) {
		if (var1.isRemote) {
			return true;
		} else {
			TileEntityWaterCollector var7 = (TileEntityWaterCollector) var1.getBlockTileEntity(var2, var3, var4);
			if (var7 != null) {
				ModLoader.openGUI(var5, new GuiWaterCollector(var5.inventory, var7));
			}
		}
		return true;
	}

	public int idDropped(int var1, Random var2, int var3) {
		return mod_ThirstMod.waterCollector.blockID;
	}

	public int quantityDropped(int var1, int var2, Random var3) {
		return 1;
	}

	protected int damageDropped(int var1) {
		return var1 < 16 ? var1 : 0;
	}

	public void onBlockRemoval(World var1, int var2, int var3, int var4) {
		TileEntityWaterCollector var5 = (TileEntityWaterCollector) var1.getBlockTileEntity(var2, var3, var4);
		if (var5 != null) {
			for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6) {
				ItemStack var7 = var5.getStackInSlot(var6);
				if (var7 != null) {
					float var8 = this.rand.nextFloat() * 0.8F + 0.1F;
					float var9 = this.rand.nextFloat() * 0.8F + 0.1F;
					float var10 = this.rand.nextFloat() * 0.8F + 0.1F;

					while (var7.stackSize > 0) {
						int var11 = this.rand.nextInt(21) + 10;
						if (var11 > var7.stackSize) {
							var11 = var7.stackSize;
						}

						var7.stackSize -= var11;
						EntityItem var12 = new EntityItem(var1, (double) ((float) var2 + var8), (double) ((float) var3 + var9), (double) ((float) var4 + var10), new ItemStack(var7.itemID, var11, var7.getItemDamage()));
						float var13 = 0.05F;
						var12.motionX = (double) ((float) this.rand.nextGaussian() * var13);
						var12.motionY = (double) ((float) this.rand.nextGaussian() * var13 + 0.2F);
						var12.motionZ = (double) ((float) this.rand.nextGaussian() * var13);
						var1.spawnEntityInWorld(var12);
					}
				}
			}
		}
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return mod_ThirstMod.WCTop;
		} else {
			return mod_ThirstMod.WCSides;
		}
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			return mod_ThirstMod.WCTop;
		}
		if (l == 0) {
			return mod_ThirstMod.WCSides;
		}
		int i1 = iblockaccess.getBlockMetadata(i, j, k);
		if (l != i1) {
			return mod_ThirstMod.WCSides;
		} else {
			return blockIndexInTexture;
		}
	}

	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}

	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}
