/**
 * This Minecraft Modification is provided for free so that it may enhance your experience. 
 * Under NO circumstances can you strip code from this class to place in your modification 
 * without permission from the authors.
 * 
 * AUTHORS: MEDIEVOR TARUN1998
 * 
 * Don't be evil. :)
 */

package net.minecraft.src.thirstmod.network;

import java.io.*;
import net.minecraft.src.thirstmod.core.*;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.forge.IPacketHandler;

public class PacketHandler implements IPacketHandler {

	public void onPacketData(NetworkManager network, String channel, byte[] data) {
		try {
			 DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
			 int[] dataInt = new int[2];
			 boolean[] dataBoolean = new boolean[1];
			 float[] dataFloat = new float[1];
			 for(int i = 0; i < 2; i++) {
				 dataInt[i] = dis.readInt();
			 }
			 for(int j = 0; j < 1; j++) {
				 dataBoolean[j] = dis.readBoolean();
				 dataFloat[j] = dis.readFloat();
			 }
			 Utilities.getStats().level = dataInt[0];
			 Utilities.getStats().healTimer = dataInt[1];
			 Utilities.getStats().poisoned = dataBoolean[0];
			 Utilities.getStats().saturation = dataFloat[1];
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
 