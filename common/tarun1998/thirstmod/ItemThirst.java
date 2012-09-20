package tarun1998.thirstmod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class ItemThirst extends Item {

	public ItemThirst(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return "/tarun1998/thirstmod/textures/icons.png";
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if(this == DrinkLoader.woodGlass) {
			MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);
			
			if(mop != null) {
				int x = mop.blockX;
				int y = mop.blockY;
				int z = mop.blockZ;
				
				if(par2World.getBlockMaterial(x, y, z) == Material.water) {
					return new ItemStack(DrinkLoader.woodWater);
				}
			}
		}
		return par1ItemStack;
	}
}
