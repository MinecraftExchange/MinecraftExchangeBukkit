package org.mcexchange;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class JoinLeavePacket extends Packet {
	//true for join, false for leave
	private boolean type;
	//network name
	private String network;
	
	public JoinLeavePacket(ServerConnection connection) {
		super(connection);
	}

	public void run() {
		// TODO get network from network list, add server IP
	}

	@Override
	public void read(SocketChannel s) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(1);
		readFull(s,b);
		setType(b.get()==1);
		setNetwork(readString(s));
	}

	@Override
	public void write(SocketChannel s) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(1);
		b.put((byte) (getType() ? 1 : 0));
		writeFull(s,b);
		writeString(s, network);
	}
	
	public boolean getType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}
	
	public String getNetwork() {
		return network;
	}
	
	public void setNetwork(String network) {
		this.network = network;
	}
	
}
