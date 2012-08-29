package net.minecraft.src.thirstmod.blocks;

import java.util.Random;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.*;
import net.minecraft.src.thirstmod.gui.GuiJM;

public class BlockJM extends BlockContainer {
	private Random random = new Random();
	private static boolean keepFreezerInventory = false;
	
	public BlockJM(int par1) {
		super(par1, Material.rock);
	}
	
	@Override
	public int idDropped(int i, Random random, int j) {
		return 233;
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer entityplayer, int j, float f, float f1, float f2) {
		entityplayer.openGui(ThirstMod.INSTANCE, 90, par1World, x, y, z);
		return true;
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		setDefaultDirection(world, i, j, k);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityJM();
	}
	
	@Override
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
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		if (!keepFreezerInventory) {
			TileEntityJM tileentityfreezer = (TileEntityJM) par1World.getBlockTileEntity(par2, par3, par4);
			label0: for (int l = 0; l < tileentityfreezer.getSizeInventory(); l++) {
				ItemStack itemstack = tileentityfreezer.getStackInSlot(l);
				if (itemstack == null) {
					continue;
				}
				float f = random.nextFloat() * 0.8F + 0.1F;
				float f1 = random.nextFloat() * 0.8F + 0.1F;
				float f2 = random.nextFloat() * 0.8F + 0.1F;
				do {
					if (itemstack.stackSize <= 0) {
						continue label0;
					}
					int i1 = random.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}
					itemstack.stackSize -= i1;
					EntityItem entityitem = new EntityItem(par1World, (float) par2 + f, (float) par3 + f1, (float) par4 + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (float) random.nextGaussian() * f3;
					entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) random.nextGaussian() * f3;
					par1World.spawnEntityInWorld(entityitem);
				} while (true);
			}
		}
	}
	
	@Override
	public int getBlockTextureFromSide(int i) {
		if (i == 3) {
			return ThirstMod.jmFront;
		} else {
			return 3;
		}
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			return 3;
		}
		int i1 = iblockaccess.getBlockMetadata(i, j, k);
		if (l != i1) {
			return 3;
		} else {
			return ThirstMod.jmFront;
		}
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

	@Override
	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}
