package tarun1998.thirstmod;

import java.io.*;
import java.util.*;

import tarun1998.thirstmod.packets.*;

import com.google.common.io.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;

public class PacketHandler implements IPacketHandler {
	public static Map playerInstance = new HashMap();
	public static boolean isRemote = false;
	// 0 = none, 1 = integrated, 2 = dedicated
	public static int typeOfServer;

	@Override
	public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player) {
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int id = dat.readInt();
		if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
			if(id == 1) ThirstMod.INSTANCE.savePacket.readServer(dat, id);
		}
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			if(id == 1) ThirstMod.INSTANCE.savePacket.readClient(dat, id);
			if(id == 2) ThirstMod.INSTANCE.soundPacket.readClient(dat, (EntityPlayer) player, id);
			if(id == 3) ThirstMod.INSTANCE.posPacket.readClient(dat, id);
		}
	}
}