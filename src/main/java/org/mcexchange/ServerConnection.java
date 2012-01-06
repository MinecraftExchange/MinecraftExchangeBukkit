package org.mcexchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.bukkit.Bukkit;

/**
 * A connection to a Server.
 */
public class ServerConnection implements Runnable {
	private final RegisteredPackets rp;
	
	private final Socket socket;
	private final DataInputStream in;
	private final DataOutputStream out;
	
	/**
	 * Creates a new connection with the given client.
	 * @param socket The client socket.
	 * @throws IOException If there is an error opening a connection to
	 * the client.
	 */
	public ServerConnection(Socket socket) throws IOException {
		rp = new RegisteredPackets(this);
		
		this.socket = socket;
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
	}
	
	/**
	 * Reads a Packet from the stream.
	 * @return The sent Packet.
	 */
	public Packet readPacket() {
		try {
			byte b = in.readByte();
			Packet p = Packet.getPacket(b);
			p.read(in);
			return p;
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			System.err.println("Client " + this + " received an un-registered packet!");
			System.err.println("Disconnecting...");
			sendPacket(rp.getDisconnect());
			disconnect();
		}
		return null;
	}
	
	/**
	 * Closes the connection.
	 */
	public void disconnect() {
		try {
			in.close();
		} catch(IOException e) {
			System.err.println("Unable to close input stream.");
			e.printStackTrace();
		}
		try {
			out.close();
		} catch(IOException e) {
			System.err.println("Unable to close output stream");
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			System.err.println("Unable to close connection.");
			e.printStackTrace();
		}
		System.out.println("Disconnected from server.");
	}
	
	/**
	 * Sends the given Packet to the client.
	 */
	public void sendPacket(Packet p) {
		try {
			byte id = Packet.getId(p);
			out.writeByte(id);
			p.write(out);
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			System.err.println("Tried to send an unregistered packet to server!");
		}
		
	}
	
	/**
	 * And the magic happens here.
	 */
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			Packet recieved = readPacket();
			Bukkit.getScheduler().scheduleSyncDelayedTask(MinecraftExchange.getInstance(), recieved);
		}
		sendPacket(rp.getDisconnect());
		disconnect();
	}
}