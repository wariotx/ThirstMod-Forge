package tarun1998.thirstmod.packets;

import java.io.*;

import tarun1998.thirstmod.ClientProxy;
import tarun1998.thirstmod.PacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import com.google.common.io.*;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketPlaySound extends PacketHandler {

    public void readClient(ByteArrayDataInput dat, EntityPlayer player, int id) {
        FMLClientHandler.instance().getClient().sndManager.playSound("random.drink", (float) player.posX, (float) player.posY, (float) player.posZ, 1f, 1f);
    }

    public static void sendPlaySound(EntityPlayer player) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(110);
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeInt(2);
        } catch (Exception e) {
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
