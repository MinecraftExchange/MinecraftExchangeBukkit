package org.mcexchange;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class MessagePacket extends Packet {
	private String message;

	public MessagePacket(ServerConnection connection) {
		super(connection);
	}

	public void run() {
		System.out.println(message);
	}

	@Override
	public void read(SocketChannel s) throws IOException {
		message = readString(s);
	}

	@Override
	public void write(SocketChannel s) throws IOException {
		writeString(s, message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
