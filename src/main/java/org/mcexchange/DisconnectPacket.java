package org.mcexchange;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Signifies disconnection.
 *
 */
public class DisconnectPacket extends Packet {
	
	public DisconnectPacket(ServerConnection connection) {
		super(connection);
	}

	public void run() {
		getConnection().disconnect();//Server quit.
	}

	@Override
	public void read(InputStream s) { }

	@Override
	public void write(OutputStream s) { }
	
}
