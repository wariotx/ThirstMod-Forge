package net.minecraft.src.thirstmod.blocks;

import java.util.Arrays;
import java.util.HashMap;
import net.minecraft.src.*;
import java.util.Map;

public class JMRecipes
{
    private static final JMRecipes solidifyingBase = new JMRecipes();
    private Map solidifyingList;
    private Map metaSmeltingList = new HashMap();

    public static final JMRecipes solidifying()
    {
        return solidifyingBase;
    }

    private JMRecipes()
    {
        solidifyingList = new HashMap();
    }

    public void addSolidifying(int i, ItemStack itemstack)
    {
        solidifyingList.put(Integer.valueOf(i), itemstack);
    }
    
    public void addSolidifying(int itemID, int metadata, ItemStack itemstack)
    {
    	metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
    }

    public ItemStack getSolidifyingResult(int i)
    {
        return (ItemStack)solidifyingList.get(Integer.valueOf(i));
    }

    public Map getSolidifyingList()
    {
        return solidifyingList;
    }
    
    public ItemStack getSmeltingResult(ItemStack item) 
    {
    	if (item == null)
   	 	{
    		return null;
   	 	}
    	ItemStack ret = (ItemStack)metaSmeltingList.get(Arrays.asList(item.itemID, item.getItemDamage()));
    	if (ret != null) 
    	{
    		return ret;
    	}
    	return (ItemStack)solidifyingList.get(Integer.valueOf(item.itemID));
    }
}
