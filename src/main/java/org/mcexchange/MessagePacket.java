package org.mcexchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessagePacket extends Packet {
	private String message;

	public MessagePacket(ServerConnection connection) {
		super(connection);
	}
	
	public void run() {
		System.out.println(message);
	}

	@Override
	public void read(DataInputStream s) throws IOException {
		setMessage(s.readUTF());
	}

	@Override
	public void write(DataOutputStream s) throws IOException {
		s.writeUTF(message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
