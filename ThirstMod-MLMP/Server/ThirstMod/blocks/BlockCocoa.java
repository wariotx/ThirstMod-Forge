package net.minecraft.src.ThirstMod.blocks;

import java.util.Random;
import net.minecraft.src.*;

public class BlockCocoa extends BlockFlower
{
	public BlockCocoa(int par1, int par2)
    {
        super(par1, par2, Material.vine);
        float f = 0.4F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
    }
	
	public int idDropped(int par1, Random par2Random, int par3)
    {
        if (par2Random.nextInt(8) == 0)
        {
            return mod_ThirstMod.cocoaBean.shiftedIndex;
        }
        else
        {
            return -1;
        }
    }
}
