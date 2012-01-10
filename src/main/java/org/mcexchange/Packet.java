package org.mcexchange;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

public abstract class Packet implements Runnable {
	//packet map.
	private static final BidiMap packets = new DualHashBidiMap();
	private final ServerConnection cc;
	
	/**
	 * Maps a Packet to an id.
	 */
	public static void assignId(byte id, Packet packet) {
		packets.put(id,packet);
	}
	
	/**
	 * Unmaps a Packet to an id.
	 */
	public static void removeId(Packet packet) {
		packets.remove(packet);
	}
	
	/**
	 * Gets a packet's id (none, if it hasn't been mapped to
	 * one yet).
	 */
	public static byte getId(Packet packet) {
		return (Byte) packets.getKey(packet);
	}
	
	/**
	 * Gets an id's packet (none if no Packet has been mapped
	 * to this id).
	 */
	public static Packet getPacket(byte id) {
		return (Packet) packets.get(id);
	}
	
	/**
	 * Creates a Packet for the given ClientConnection.
	 */
	public Packet(ServerConnection connection) {
		cc = connection;
	}
	
	public ServerConnection getConnection() {
		return cc;
	}
	
	/**
	 * Utility method for filling the remainder of the given ByteBuffer with
	 * data read from the given channel.
	 * @throws IOException 
	 */
	public final void readFull(SocketChannel s, ByteBuffer b) throws IOException {
		while(b.remaining()>0) s.read(b);
	}
	
	/**
	 * Utility method for writing the remainder of the given ByteBuffer to the
	 * given channel.
	 * @throws IOException 
	 */
	public final void writeFull(SocketChannel s, ByteBuffer b) throws IOException {
		while(b.remaining()>0) s.write(b);
	}
	
	/**
	 * Utility method for reading a String from a SocketChannel.
	 * @throws IOException 
	 */
	public final String readString(SocketChannel s) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(2);
		readFull(s,b);
		b.flip();
		short size = b.getShort();
		b = ByteBuffer.allocate(size);
		readFull(s,b);
		b.flip();
		byte[] bytes = new byte[size];
		b.get(bytes);
		return  new String(bytes);
	}
	
	/**
	 * Utility method for writing a String to a SocketChannel.
	 * @throws IOException 
	 */
	public final void writeString(SocketChannel s, String message) throws IOException {
		byte[] bytes = message.getBytes();
		short size = (short) bytes.length;
		ByteBuffer b = ByteBuffer.allocate(size + 2);
		b.putShort(size);
		b.put(bytes);
		b.flip();
		writeFull(s, b);
	}
	
	/**
	 * Reads the Packet's data from the stream. Only one instance (of each type)
	 * should be needed for each ClientConnection, so this method can be called
	 * on an already sent packet to "reset" it to the next Packet.
	 */
	public abstract void read(SocketChannel s) throws IOException;
	
	/**
	 * Writes the Packet to the stream.
	 */
	public abstract void write(SocketChannel s) throws IOException;
}
