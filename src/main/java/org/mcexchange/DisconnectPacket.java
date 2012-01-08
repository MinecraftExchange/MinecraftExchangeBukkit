package org.mcexchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Signifies disconnection.
 *
 */
public class DisconnectPacket extends Packet {
	
	public DisconnectPacket(ServerConnection connection) {
		super(connection);
	}

	public void run() {
		Thread.currentThread().interrupt();
	}

	@Override
	public void read(DataInputStream s) { }

	@Override
	public void write(DataOutputStream s) { }
	
}
