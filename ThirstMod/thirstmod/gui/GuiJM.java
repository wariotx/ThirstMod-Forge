package net.minecraft.src.thirstmod.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.thirstmod.blocks.*;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class GuiJM extends GuiContainer
{
    private TileEntityJM freezerInventory;

    public GuiJM(InventoryPlayer inventoryplayer, TileEntityJM tileentityfreezer)
    {
        super(new ContainerJM(inventoryplayer, tileentityfreezer));
        freezerInventory = tileentityfreezer;
        mc = FMLClientHandler.instance().getClient();
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Drinks Brewer", 54, 10, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/ThirstMod/textures/drinksBrewer.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        if (freezerInventory.makeTime > 0)
        {
        	int m = freezerInventory.getFreezeTimeRemainingScaled(12);
            drawTexturedModalRect(188, 85, 176, 31, 8, m);
        }
        
        int k1 = freezerInventory.getCoolProgressScaled(24);
		drawTexturedModalRect(l + 79, i1 + 34, 176, 14, k1 + 1, 16);
	}
}
