package org.mcexchange;

/**
 * Used to provide access to built-in packets.
 *
 */
public class RegisteredPackets {
	private DisconnectPacket disconnect;
	
	public RegisteredPackets(ServerConnection sc) {
		disconnect = new DisconnectPacket(sc);
		Packet.assignId((byte)-127, disconnect);
	}
	
	public Packet getDisconnect() {
		return disconnect;
	}
}
