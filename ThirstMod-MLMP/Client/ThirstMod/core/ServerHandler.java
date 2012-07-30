package net.minecraft.src.ThirstMod.core;

import net.minecraft.src.*;

public class ServerHandler
{
	public void handlePacket(Packet230ModLoader packet)
	{
		switch (packet.packetType)
		{
			case 0:
			{
				ThirstUtils.getStats().thirstLevel = packet.dataInt[0];
				ThirstUtils.getStats().thirstTickTimer = packet.dataInt[1];
				ThirstUtils.getStats().thirstDuration = packet.dataInt[2];
				ThirstUtils.getStats().isThirstPoisoned = (packet.dataInt[3] == 1 ? true : false);
				ThirstModCore.time = packet.dataInt[4];

				ThirstUtils.getStats().thirstSaturationLevel1 = packet.dataFloat[0];
				ThirstUtils.getStats().thirstExhaustionLevel = packet.dataFloat[1];
			}
		}
	}
}
