package org.mcexchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	public void read(DataInputStream s) throws IOException {
		setType(s.readBoolean());
		setNetwork(s.readUTF());
	}

	@Override
	public void write(DataOutputStream s) throws IOException {
		s.writeBoolean(type);
		s.writeUTF(network);
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
