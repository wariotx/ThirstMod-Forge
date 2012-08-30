package net.minecraft.src.thirstmod;

import net.minecraft.src.Item;

public class ItemThirst extends Item {
	
	public ItemThirst(int id) {
		super(id);
	}
	
	@Override
	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}
