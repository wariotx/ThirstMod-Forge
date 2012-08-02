/**
 * Code belongs to CPW 
 */

package net.minecraft.src.thirstmod.overrides;

import net.minecraft.src.TextureFX;

public class TextureInfo
{
    public String texture;
    public String override;
    public int index;
    public int imageIndex;
    public TextureFX textureFX;
    public boolean added;
    
    public boolean equals(Object obj)
    {
        try {
        	TextureInfo inf=(TextureInfo) obj;
            return index==inf.index && imageIndex==inf.imageIndex;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public int hashCode()
    {
        return index+imageIndex;
    }
}