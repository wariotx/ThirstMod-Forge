package tarun1998.thirstmod.packets;

import java.io.*;
import net.minecraft.src.*;
import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import tarun1998.thirstmod.PacketHandler;
import tarun1998.thirstmod.utils.ThirstUtils;

public class PacketPlayerPos extends PacketHandler {
	public static String currentBiome;
	
	public void readClient(ByteArrayDataInput data, int id) {
		currentBiome = data.readUTF();
	}
	
	public static void sendPlayerPos(EntityPlayer player) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeInt(3);
			dos.writeUTF(ThirstUtils.getCurrentBiome(player));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "ThirstMod";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
		
		PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
	}
}
