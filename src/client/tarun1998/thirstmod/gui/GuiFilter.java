package tarun1998.thirstmod.gui;

import org.lwjgl.opengl.GL11;

import tarun1998.thirstmod.blocks.ContainerFilter;
import tarun1998.thirstmod.blocks.TileEntityFilter;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;

public class GuiFilter extends GuiContainer {
	public TileEntityFilter filter;
	
	public GuiFilter(InventoryPlayer inv, TileEntityFilter tile) {
		super(new ContainerFilter(inv, tile));
		this.filter = tile;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer() {
		mc.fontRenderer.drawString("Filter", 60, 6, 0x404040);
		mc.fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int var4 = this.mc.renderEngine.getTexture("/tarun1998/thirstmod/textures/filter.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}
}
