package tarun1998.thirstmod.blocks;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class GuiRC extends GuiContainer {
	private TileEntityRC rc;
	private Minecraft minecraft = FMLClientHandler.instance().getClient();

	public GuiRC(InventoryPlayer var1, TileEntityRC var2) {
		super(new ContainerRC(var1, var2));
		rc = var2;
		mc = minecraft;
	}
	
	protected void drawGuiContainerForegroundLayer()
    {
        minecraft.fontRenderer.drawString("Rain Collector", 60, 6, 0x404040);
        minecraft.fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		World world = minecraft.theWorld;
        int var4 = minecraft.renderEngine.getTexture("/tarun1998/thirstmod/textures/waterCollector.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.renderEngine.bindTexture(var4);
        int var5 = (width - xSize) / 2;
        int var6 = (height - ySize) / 2;
        int var7 = rc.getInternalBucketScaled(12);
        int var8 = rc.getRainMeterScaled(24);
        drawTexturedModalRect(var5, var6, 0, 0, xSize, ySize);
        drawTexturedModalRect(var5 + 57, var6 + 36 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
        drawTexturedModalRect(var5 + 79, var6 + 34, 176, 14, var8 + 1, 16);
        if(rc.canRainOn(rc.xCoord, rc.yCoord, rc.zCoord, world) && world.getWorldInfo().isRaining()) {
        	drawTexturedModalRect(var5 + 55, var6 + 16, 176, 31, 18, 18);
        }
	}
}
