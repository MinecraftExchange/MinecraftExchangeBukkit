package org.mcexchange;

import java.io.IOException;
import java.nio.ByteBuffer;
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
		ByteBuffer b = ByteBuffer.allocate(2);
		while(b.remaining()>0) s.read(b);
		b.flip();
		short size = b.getShort();
		b = ByteBuffer.allocate(size);
		while(b.remaining()>0) s.read(b);
		b.flip();
		byte[] bytes = new byte[size];
		b.get(bytes);
		message = new String(bytes);
	}

	@Override
	public void write(SocketChannel s) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(2 * (message.length() + 1));
		b.putShort((short) message.length());
		b.asCharBuffer().append(message);
		b.flip();
		s.write(b);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
