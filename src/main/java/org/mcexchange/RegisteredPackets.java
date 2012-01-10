package org.mcexchange;

/**
 * Used to provide access to built-in packets.
 *
 */
public class RegisteredPackets {
	private DisconnectPacket disconnect;
	private MessagePacket message;
	
	public RegisteredPackets(ServerConnection sc) {
		disconnect = (DisconnectPacket) Packet.getPacket((byte) -127);
		if(disconnect == null) {
			disconnect = new DisconnectPacket(sc);
			Packet.assignId((byte)-127, disconnect);
		}
		message = (MessagePacket) Packet.getPacket((byte) -126);
		if(message==null) {
			message = new MessagePacket(sc);
			Packet.assignId((byte)-126, message);
		}
	}
	
	public DisconnectPacket getDisconnect() {
		return disconnect;
	}
	
	public MessagePacket getMessage() {
		return message;
	}
}
