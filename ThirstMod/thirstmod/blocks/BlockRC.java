package net.minecraft.src.thirstmod.blocks;

import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.ThirstUtils;

public class BlockRC extends BlockContainer {
	private static boolean keepRCInventory = false;
	private Random rand = new Random();

	public BlockRC(int id) {
		super(id, Material.rock);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3) {
		return ThirstMod.waterCollector.blockID;
	}
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		}

		TileEntityRC tileentityfurnace = (TileEntityRC) par1World.getBlockTileEntity(par2, par3, par4);

		if (tileentityfurnace != null) {
			ModLoader.getMinecraftInstance().displayGuiScreen(new GuiRC(ThirstUtils.getPlayerMp().inventory, tileentityfurnace));
		}

		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityRC();
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		super.onBlockAdded(par1World, par2, par3, par4);
		setDefaultDirection(par1World, par2, par3, par4);
	}

	private void setDefaultDirection(World par1World, int par2, int par3, int par4) {
		if (par1World.isRemote) {
			return;
		}

		int i = par1World.getBlockId(par2, par3, par4 - 1);
		int j = par1World.getBlockId(par2, par3, par4 + 1);
		int k = par1World.getBlockId(par2 - 1, par3, par4);
		int l = par1World.getBlockId(par2 + 1, par3, par4);
		byte byte0 = 3;

		if (Block.opaqueCubeLookup[i] && !Block.opaqueCubeLookup[j]) {
			byte0 = 3;
		}

		if (Block.opaqueCubeLookup[j] && !Block.opaqueCubeLookup[i]) {
			byte0 = 2;
		}

		if (Block.opaqueCubeLookup[k] && !Block.opaqueCubeLookup[l]) {
			byte0 = 5;
		}

		if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[k]) {
			byte0 = 4;
		}

		par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0);
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving) {
		int i = MathHelper.floor_double((double) ((par5EntityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

		if (i == 0) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2);
		}

		if (i == 1) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 5);
		}

		if (i == 2) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3);
		}

		if (i == 3) {
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 4);
		}
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
		if (!keepRCInventory) {
			TileEntityRC tileentityfurnace = (TileEntityRC) par1World.getBlockTileEntity(par2, par3, par4);

			if (tileentityfurnace != null) {
				label0:

				for (int i = 0; i < tileentityfurnace.getSizeInventory(); i++) {
					ItemStack itemstack = tileentityfurnace.getStackInSlot(i);

					if (itemstack == null) {
						continue;
					}

					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;

					do {
						if (itemstack.stackSize <= 0) {
							continue label0;
						}

						int j = rand.nextInt(21) + 10;

						if (j > itemstack.stackSize) {
							j = itemstack.stackSize;
						}

						itemstack.stackSize -= j;
						EntityItem entityitem = new EntityItem(par1World, (float) par2 + f, (float) par3 + f1, (float) par4 + f2, new ItemStack(itemstack.itemID, j, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.item.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (float) rand.nextGaussian() * f3;
						entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
						entityitem.motionZ = (float) rand.nextGaussian() * f3;
						par1World.spawnEntityInWorld(entityitem);
					} while (true);
				}
			}
		}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public int getBlockTextureFromSide(int i) {
		if (i == 1) {
			return ThirstMod.rcTop;
		} else {
			return 3;
		}
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		if (l == 1) {
			return ThirstMod.rcTop;
		}
		return 3;
	}
	
	@Override
	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}
