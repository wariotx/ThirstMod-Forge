package tarun1998.thirstmod.blocks;

import java.util.Random;

import tarun1998.thirstmod.ThirstMod;

import net.minecraft.src.*;

public class BlockFilter extends BlockRC {

    public BlockFilter(int par1) {
        super(par1);
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
        return this.blockID;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        par5EntityPlayer.openGui(ThirstMod.INSTANCE, 92, par1World, par2, par3, par4);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityFilter();
    }

    @Override
    public int getBlockTextureFromSide(int i) {
        if (i == 3) {
            return 4;
        } else {
            return 3;
        }
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (l == 3) {
            return 4;
        }
        return 3;
    }
}
