package net.minecraft.src.thirstmod.gui;

import java.awt.Desktop;
import java.net.*;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;

public class GuiUpdate extends GuiScreen
{
    /**
     * The cooldown timer for the buttons, increases every tick and enables all buttons when reaching 20.
     */
    private int cooldownTimer;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();

        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 72, "Yes"));
        controlList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96, "No"));

        if (mc.session == null)
        {
            ((GuiButton)controlList.get(1)).enabled = false;
        }

        for (Iterator iterator = controlList.iterator(); iterator.hasNext();)
        {
        	GuiButton guibutton = (GuiButton)iterator.next();
        	guibutton.enabled = false;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 1:
               openUrl();
               mc.displayGuiScreen(null);
                break;
            case 2:
                mc.displayGuiScreen(null);
                break;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
    	fontRenderer.FONT_HEIGHT = 10;
        drawGradientRect(0, 0, width, height, 0x60500000, 0xa0803030);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        drawCenteredString(fontRenderer, "Do you want to update ThirstMod", width / 2 / 2, 40, 0xffffff);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        cooldownTimer++;

        if (cooldownTimer == 20)
        {
            for (Iterator iterator = controlList.iterator(); iterator.hasNext();)
            {
                GuiButton guibutton = (GuiButton)iterator.next();
                guibutton.enabled = true;
            }
        }
    }
    
    private void openUrl() {
		try {
			URL thirstMod = new URL("http://www.minecraftforum.net/topic/1143097-132-ssplan-thirst-mod-finally-updated-now-with-custom-drinks/");
			URI uri = thirstMod.toURI();
			Desktop.getDesktop().browse(uri);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
