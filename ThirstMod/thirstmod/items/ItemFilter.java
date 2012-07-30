package net.minecraft.src.thirstmod.items;

import net.minecraft.src.Item;

public class ItemFilter extends Item {
	
	public ItemFilter(int id) {
		super(id);
		setMaxStackSize(1);
	}
	
	public String getTextureFile() {
		return "/thirstmod/textures/icons.png";
	}
}
