package net.minecraft.src.thirstmod.network;

import java.io.*;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.forge.IPacketHandler;
import net.minecraft.src.thirstmod.Utilities;

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
 