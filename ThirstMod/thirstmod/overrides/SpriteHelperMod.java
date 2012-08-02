/**
 * Code belongs to CPW
 */

package net.minecraft.src.thirstmod.overrides;

import java.util.BitSet;
import java.util.HashMap;

import net.minecraft.src.ModLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderException;

/**
 * @author cpw
 *
 */
public class SpriteHelperMod
{
    private static HashMap<String, BitSet> spriteInfo = new HashMap<String, BitSet>();

    private static void initMCSpriteMaps() {
        BitSet slots = 
                SpriteHelperMod.toBitSet("0000000000000000"
				+ "0000000000000000" + "0000000000000000" + "0000000000000000"
				+ "0000000000000000" + "0000000000000000" + "0000001000000000"
				+ "0000001110000000" + "0000001000000000" + "1111111010000000"
				+ "1111111010100000" + "1111111111111100" + "1111111111111111"
				+ "1111111111111111" + "1111111111111111" + "0000000000000000");
        spriteInfo.put("/thirstmod/textures/content.png", slots);
    }
    /**
     * Register a sprite map for ModTextureStatic, to allow for other mods to override
     * your sprite page.
     * 
     * 
     */
    public static void registerSpriteMapForFile(String file, String spriteMap) {
        if (spriteInfo.size() == 0) {
            initMCSpriteMaps();
        }
        if (spriteInfo.containsKey(file)) {
            return;
        }
        spriteInfo.put(file, toBitSet(spriteMap));
    }
    
    public static int getUniqueSpriteIndex(String path)
    {
        if (!spriteInfo.containsKey("/thirstmod/textures/content.png"))
        {
            initMCSpriteMaps();
        }
        
        BitSet slots = spriteInfo.get(path);
        
        if (slots == null)
        {
            Exception ex = new Exception(String.format("Invalid getUniqueSpriteIndex call for texture: %s", path));
            Loader.log.throwing("ModLoader", "getUniqueSpriteIndex", ex);
        }

        int ret = getFreeSlot(slots);

        if (ret == -1)
        {
            Exception ex = new Exception(String.format("No more sprite indicies left for: %s", path));
            Loader.log.throwing("ModLoader", "getUniqueSpriteIndex", ex);
        }
        return ret;
    }

    public static BitSet toBitSet(String data)
    {
        BitSet ret = new BitSet(data.length());
        for (int x = 0; x < data.length(); x++)
        {
            ret.set(x, data.charAt(x) == '1');
        }
        return ret;
    }

    public static int getFreeSlot(BitSet slots)
    {
        int next=slots.nextSetBit(0);
        slots.clear(next);
        return next;
    }

    /**
     * @param textureToOverride
     * @return
     */
    public static int freeSlotCount(String textureToOverride)
    {
        return spriteInfo.get(textureToOverride).cardinality();
    }

}
