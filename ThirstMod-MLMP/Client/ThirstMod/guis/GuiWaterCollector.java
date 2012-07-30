package net.minecraft.src.ThirstMod.guis;

import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;
import net.minecraft.src.ThirstMod.containers.ContainerWaterCollector;
import net.minecraft.src.ThirstMod.containers.TileEntityWaterCollector;

public class GuiWaterCollector extends GuiContainer
{
    private TileEntityWaterCollector rc;

    public GuiWaterCollector(InventoryPlayer var1, TileEntityWaterCollector var2)
    {
        super(new ContainerWaterCollector(var1, var2));
        this.rc = var2;
    }
    
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Rain Collector", 60, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
    	World world = ModLoader.getMinecraftInstance().theWorld;
        int var4 = this.mc.renderEngine.getTexture("/ThirstMod/textures/waterCollector.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        int var7 = this.rc.getInternalBucketScaled(12);
        int var8 = this.rc.getRainMeterScaled(24);
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(var5 + 57, var6 + 36 + 12 - var7, 176, 12 - var7, 14, var7 + 2);
        this.drawTexturedModalRect(var5 + 79, var6 + 34, 176, 14, var8 + 1, 16);
        if (rc.canRainOn(rc.xCoord, rc.yCoord, rc.zCoord, world) && !world.isRemote)
        {
            this.drawTexturedModalRect(var5 + 55, var6 + 16, 176, 31, 18, 18);
        }
        
        if(mod_ThirstMod.canRainOn == true && world.isRemote)
        {
        	this.drawTexturedModalRect(var5 + 55, var6 + 16, 176, 31, 18, 18);
        }
    }
}
