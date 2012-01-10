package org.mcexchange;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.bukkit.Bukkit;

/**
 * A connection to a Server.
 */
public class ServerConnection implements Runnable {
	private final RegisteredPackets rp;
	
	private final SocketChannel channel;
	private final ByteBuffer readId = ByteBuffer.allocateDirect(1);
	private final ByteBuffer writeId = ByteBuffer.allocateDirect(1);
	
	/**
	 * Creates a new connection with the given client.
	 * @param socket The client socket.
	 * @throws IOException If there is an error opening a connection to
	 * the client.
	 */
	public ServerConnection(SocketChannel channel) throws IOException {
		rp = new RegisteredPackets(this);
		
		this.channel = channel;
	}
	
	/**
	 * Reads a Packet from the stream.
	 * @return The sent Packet.
	 */
	public Packet readPacket() {
		try {
			readId.clear();
			channel.read(readId);
			readId.flip();
			byte temp = readId.get();
			System.out.println("Packet recieved: " + temp + "!");
			Packet p = Packet.getPacket(temp);
			p.read(channel);
			return p;
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			e.printStackTrace();
			System.err.println("Client " + this + " received an un-registered packet!");
			System.err.println("Disconnecting...");
			Thread.currentThread().interrupt();
		}
		return null;
	}
	
	/**
	 * Closes the connection.
	 */
	public void disconnect() {
		try {
			channel.close();
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
			writeId.clear();
			writeId.put(Packet.getId(p));
			writeId.flip();
			channel.write(writeId);
			p.write(channel);
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			e.printStackTrace();
			System.err.println("Tried to send an unregistered packet to Client " + this + "!");
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
