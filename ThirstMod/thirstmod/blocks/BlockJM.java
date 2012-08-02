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
import net.minecraft.src.thirstmod.containers.*;
import net.minecraft.src.thirstmod.guis.GuiJM;
import net.minecraft.src.*;

public class BlockJM extends BlockContainer implements ITextureProvider {
	private Random freezerRand;
	private final boolean isActive;
	private static boolean keepFreezerInventory = false;

	public BlockJM(int i, boolean flag) {
		super(i, Material.rock);
		freezerRand = new Random();
		isActive = flag;
		blockIndexInTexture = 0;
	}

	public int idDropped(int i, Random random) {
		return mod_ThirstMod.juiceMaker.blockID;
	}

	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		setDefaultDirection(world, i, j, k);
	}

	private void setDefaultDirection(World world, int i, int j, int k) {
		if (world.isRemote) {
			return;
		}
		int l = world.getBlockId(i, j, k - 1);
		int i1 = world.getBlockId(i, j, k + 1);
		int j1 = world.getBlockId(i - 1, j, k);
		int k1 = world.getBlockId(i + 1, j, k);
		byte byte0 = 3;
		if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1]) {
			byte0 = 3;
		}
		if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l]) {
			byte0 = 2;
		}
		if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1]) {
			byte0 = 5;
		}
		if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1]) {
			byte0 = 4;
		}
		world.setBlockMetadataWithNotify(i, j, k, byte0);
	}

	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
		if (world.isRemote) {
			return true;
		} else {
			TileEntityJM tileentityfreezer = (TileEntityJM) world.getBlockTileEntity(i, j, k);
			ModLoader.openGUI(entityplayer, new GuiJM(entityplayer.inventory, tileentityfreezer));
			return true;
		}
	}

	public TileEntity getBlockEntity() {
		return new TileEntityJM();
	}

	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving) {
		int l = MathHelper.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		if (l == 0) {
			world.setBlockMetadataWithNotify(i, j, k, 2);
		}
		if (l == 1) {
			world.setBlockMetadataWithNotify(i, j, k, 5);
		}
		if (l == 2) {
			world.setBlockMetadataWithNotify(i, j, k, 3);
		}
		if (l == 3) {
			world.setBlockMetadataWithNotify(i, j, k, 4);
		}
	}

	public void onBlockRemoval(World world, int i, int j, int k) {
		if (!keepFreezerInventory) {
			TileEntityJM tileentityfreezer = (TileEntityJM) world.getBlockTileEntity(i, j, k);
			label0: for (int l = 0; l < tileentityfreezer.getSizeInventory(); l++) {
				ItemStack itemstack = tileentityfreezer.getStackInSlot(l);
				if (itemstack == null) {
					continue;
				}
				float f = freezerRand.nextFloat() * 0.8F + 0.1F;
				float f1 = freezerRand.nextFloat() * 0.8F + 0.1F;
				float f2 = freezerRand.nextFloat() * 0.8F + 0.1F;
				do {
					if (itemstack.stackSize <= 0) {
						continue label0;
					}
					int i1 = freezerRand.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) freezerRand.nextGaussian() * f3;
					entityitem.motionY = (float) freezerRand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) freezerRand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				} while (true);
			}
		}
		super.onBlockRemoval(world, i, j, k);
	}

	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return mod_ThirstMod.JMTop;
		}
		if (i == 3) {
			return mod_ThirstMod.JMFront;
		} else {
			return mod_ThirstMod.JMSides;
		}
	}

	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			return mod_ThirstMod.JMTop;
		}
		int i1 = iblockaccess.getBlockMetadata(i, j, k);
		if (l != i1) {
			return mod_ThirstMod.JMSides;
		} else {
			return mod_ThirstMod.JMFront;
		}
	}

	public void addCreativeItems(ArrayList itemList) {
		itemList.add(new ItemStack(this));
	}

	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}
