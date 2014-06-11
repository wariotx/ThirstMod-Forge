package tarun1998.thirstmod.packets;

import java.io.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import tarun1998.thirstmod.*;
import tarun1998.thirstmod.utils.*;

public class PacketHandleSave extends PacketHandler implements IConnectionHandler {

    public void readClient(ByteArrayDataInput dat, int id) {
        int level = dat.readInt();
        int healTimer = dat.readInt();
        int drinkTimer = dat.readInt();
        float saturation = dat.readFloat();
        float exhaustion = dat.readFloat();
        String playerName = dat.readLine();

        if (!playerInstance.containsKey(playerName)) {
            ThirstUtils.addNewPlayer(playerName, new ThirstUtils());
        }

        if (ClientProxy.getPlayer().username.equals(playerName)) {
            ThirstUtils.getUtilsFor(playerName).getStats().level = level;
            ThirstUtils.getUtilsFor(playerName).getStats().healhurtTimer = healTimer;
            ThirstUtils.getUtilsFor(playerName).getStats().drinkTimer = drinkTimer;
            ThirstUtils.getUtilsFor(playerName).getStats().saturation = saturation;
            ThirstUtils.getUtilsFor(playerName).getStats().exhaustion = exhaustion;
        }
    }

    public void readServer(ByteArrayDataInput dat, int id) {
        int level = dat.readInt();
        int healTimer = dat.readInt();
        int drinkTimer = dat.readInt();
        float saturation = dat.readFloat();
        float exhaustion = dat.readFloat();
        String playerName = dat.readLine();

        writeData(level, healTimer, drinkTimer, saturation, exhaustion, playerName);

        PlayerStatisticsMP stats = ThirstUtils.getUtilsFor(playerName).statsMp;
        stats.level = level;
        stats.healhurtTimer = healTimer;
        stats.drinkTimer = drinkTimer;
        stats.saturation = saturation;
        stats.exhaustion = exhaustion;
    }

    /**
     * Writes all the data from a packet to the nbt.
     *
     * @param i level
     * @param j healTimer
     * @param k drinkTimer
     * @param f saturation
     * @param l exhaustion
     * @param s playerName
     */
    public void writeData(int i, int j, int k, float f, float g, String s) {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(s);
        NBTTagCompound nbt = player.getEntityData();
        nbt.setInteger("tmLevel", i);
        nbt.setFloat("tmExhaustion", g);
        nbt.setFloat("tmSaturation", f);
        nbt.setInteger("tmTimer", k);
        nbt.setInteger("tmTimer2", j);
    }

    /**
     * Reads data from an individual player nbt storage.
     *
     * @param s player username
     */
    public void readData(String s) {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(s);
        NBTTagCompound nbt = player.getEntityData();
        if (nbt.hasKey("tmLevel")) {
            ThirstUtils.getUtilsFor(s).getStats().level = nbt.getInteger("tmLevel");
            ThirstUtils.getUtilsFor(s).getStats().exhaustion = nbt.getFloat("tmExhaustion");
            ThirstUtils.getUtilsFor(s).getStats().saturation = nbt.getFloat("tmSaturation");
            ThirstUtils.getUtilsFor(s).getStats().healhurtTimer = nbt.getInteger("tmTimer");
            ThirstUtils.getUtilsFor(s).getStats().drinkTimer = nbt.getInteger("tmTimer2");
        } else {
            ThirstUtils.getUtilsFor(s).setDefaults();
        }
        sendSaveData(s, ThirstUtils.getUtilsFor(s).getStats());
    }

    /**
     * Sends data for a specific player back to the client or server.
     *
     * @param s     username
     * @param stats instance
     */
    public static void sendSaveData(String s, PlayerStatistics stats) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeInt(1);
            dos.writeInt(stats.level);
            dos.writeInt(stats.healhurtTimer);
            dos.writeInt(stats.drinkTimer);
            dos.writeFloat(stats.saturation);
            dos.writeFloat(stats.exhaustion);
            dos.writeBytes(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Packet250CustomPayload pkt = new Packet250CustomPayload();
        pkt.channel = "ThirstMod";
        pkt.data = bos.toByteArray();
        pkt.length = bos.size();
        pkt.isChunkDataPacket = false;
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) PacketDispatcher.sendPacketToServer(pkt);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(s);
            PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
        }
    }

    @Override
    public void playerLoggedIn(Player other, NetHandler netHandler, NetworkManager manager) {
        EntityPlayer player = (EntityPlayer) other;
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            playerInstance.put(player.username, new ThirstUtils());
            readData(player.username);
        }
    }

    @Override
    public void connectionClosed(NetworkManager manager) {
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, NetworkManager manager) {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, NetworkManager manager) {
        isRemote = true;
        typeOfServer = 2;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, NetworkManager manager) {
        typeOfServer = 1;
        isRemote = true;
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, NetworkManager manager, Packet1Login login) {
    }
}
