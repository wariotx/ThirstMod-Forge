package tarun1998.thirstmod.utils;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;
import tarun1998.thirstmod.ThirstMod;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.src.KeyBinding;

public class KeyHandler extends cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler {

    public static KeyBinding myBinding = new KeyBinding("TM On/Off", Keyboard.KEY_L);
    public boolean doneStuff = false;

    public KeyHandler() {
        super(new KeyBinding[]{myBinding}, new boolean[]{false});
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        if (FMLClientHandler.instance().getClient().currentScreen == null) {
            if (doneStuff == false) {
                ThirstMod.modOff = !ThirstMod.modOff;
                if (ThirstMod.modOff == false) {
                    ThirstMod.shouldTellPlayer1 = true;
                    doneStuff = true;
                }

                if (ThirstMod.modOff == true) {
                    ThirstMod.shouldTellPlayer0 = true;
                    doneStuff = true;
                    return;
                }
            }
        }
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
        doneStuff = false;
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.PLAYER);
    }
}