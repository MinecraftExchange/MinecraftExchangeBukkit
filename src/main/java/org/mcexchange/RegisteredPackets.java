package org.mcexchange;

/**
 * Used to provide access to built-in packets.
 *
 */
public class RegisteredPackets {
	private DisconnectPacket disconnect;
	private MessagePacket message;
	
	public RegisteredPackets(ServerConnection sc) {
		disconnect = new DisconnectPacket(sc);
		Packet.assignId((byte)-127, disconnect);
		message = new MessagePacket(sc);
		Packet.assignId((byte)-126, disconnect);
	}
	
	public DisconnectPacket getDisconnect() {
		return disconnect;
	}
	
	public MessagePacket getMessage() {
		return message;
	}
}
