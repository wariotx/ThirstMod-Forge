/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.content;

import java.io.File;
import java.io.FilenameFilter;

public class ExtensionFilter implements FilenameFilter 
{
	private String extension;

	public ExtensionFilter(String extension) 
	{
		this.extension = extension;
	}

	public boolean accept(File dir, String name) 
	{
		return (name.endsWith(extension));
	}
}