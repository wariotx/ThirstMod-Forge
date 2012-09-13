package tarun1998.thirstmod;

import java.io.*;
import java.util.*;
import com.google.common.io.*;
import net.minecraft.src.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.*;

/**
 * This class is here so that data is saved for a specific player is sent to the dedicated server so that it is saved.
 */

public class PacketHandler implements IPacketHandler, IPlayerTracker {
	public static Map playerInstance = new HashMap();
	
	@Override
	public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(FMLCommonHandler.instance().getSide() == Side.SERVER) {
			ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
			int level = dat.readInt();
			int healTimer = dat.readInt();
			int drinkTimer = dat.readInt();
			float saturation = dat.readFloat();
			float exhaustion = dat.readFloat();
			String playerName = dat.readLine();
			writeData(level, healTimer, drinkTimer, saturation, exhaustion, playerName);
		}
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
			int level = dat.readInt();
			int healTimer = dat.readInt();
			int drinkTimer = dat.readInt();
			float saturation = dat.readFloat();
			float exhaustion = dat.readFloat();
			String playerName = dat.readLine();
			
			ThirstUtils.getStats().level = level;
			ThirstUtils.getStats().healhurtTimer = healTimer;
			ThirstUtils.getStats().drinkTimer = drinkTimer;
			ThirstUtils.getStats().saturation = saturation;
			ThirstUtils.getStats().exhaustion = exhaustion;
		}
	}
	
	/**
	 * Writes all the data from a packet to the nbt.
	 * @param i level
	 * @param j healTimer
	 * @param k drinkTimer
	 * @param f saturation
	 * @param l exhaustion
	 * @param s playerName
	 */
	public void writeData(int i, int j, int k, float f, float l, String s) {
		EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(s);
		NBTTagCompound nbt = player.getEntityData();
		nbt.setInteger("tmLevel", i);
		nbt.setFloat("tmExhaustion", l);
		nbt.setFloat("tmSaturation", f);
		nbt.setInteger("tmTimer", k);
		nbt.setInteger("tmTimer2", j);
	}
	
	/**
	 * Reads data from an individual player nbt storage.
	 * @param s player username
	 */
	public void readData(String s) {
		EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(s);
		NBTTagCompound nbt = player.getEntityData();
		ThirstUtils utils = (ThirstUtils) playerInstance.get(s);
		if(nbt.hasKey("tmLevel")) {
			utils.getStats().level = nbt.getInteger("tmLevel");
			utils.getStats().exhaustion = nbt.getFloat("tmExhaustion");
			utils.getStats().saturation = nbt.getFloat("tmSaturation");
			utils.getStats().healhurtTimer = nbt.getInteger("tmTimer");
			utils.getStats().drinkTimer = nbt.getInteger("tmTimer2");
		} else {
			utils.setDefaults();
		}
		sendData(s, utils.getStats());
	}
	
	/**
	 * Sends data for a specific player back to the client or server.
	 * @param s
	 * @param stats
	 */
	public void sendData(String s, PlayerStatistics stats) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeInt(stats.level);
			dos.writeInt(stats.healhurtTimer);
			dos.writeInt(stats.drinkTimer);
			dos.writeFloat(stats.saturation);
			dos.writeFloat(stats.exhaustion);
			dos.writeBytes(s);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Packet250CustomPayload pkt = new Packet250CustomPayload();
		pkt.channel = "ThirstMod";
		pkt.data = bos.toByteArray();
		pkt.length = bos.size();
		pkt.isChunkDataPacket = false;
		FMLClientHandler.instance().sendPacket(pkt);
	}

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if(FMLCommonHandler.instance().getSide() == Side.SERVER) {
			playerInstance.put(player.username, new ThirstUtils());
			readData(player.username);
			ThirstUtils utils = (ThirstUtils) playerInstance.get(player.username);
			sendData(player.username, utils.getStats());
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			sendData(player.username, ThirstUtils.getStats());
		}
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
		ThirstUtils utils = (ThirstUtils) playerInstance.get(player.username);
		utils.setDefaults();
	}
}
