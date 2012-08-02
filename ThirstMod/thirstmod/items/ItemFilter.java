/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

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
