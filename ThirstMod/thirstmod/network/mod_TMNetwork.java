package net.minecraft.src.thirstmod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.forge.*;

public class mod_TMNetwork extends NetworkMod implements IConnectionHandler {

	public String getVersion() {
		return "ThirstMod Forge Network V1";
	}

	public void load() {
		MinecraftForge.registerConnectionHandler(this);
	}
	
	public boolean clientSideRequired() {
		return true;
	}
	
	public boolean serverSideRequired() {
		return false;
	}

	public void onConnect(NetworkManager network) {
		MessageManager.getInstance().registerChannel(network, new PacketHandlerClient(), "TM Network");
	}

	public void onLogin(NetworkManager network, Packet1Login login) {
		
	}

	public void onDisconnect(NetworkManager network, String message, Object[] args) {
		MessageManager.getInstance().removeConnection(network);
	}
}
