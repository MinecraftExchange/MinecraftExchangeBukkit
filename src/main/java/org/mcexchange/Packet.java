package org.mcexchange;

import java.io.InputStream;
import java.io.OutputStream;

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
	 * Reads the Packet's data from the stream. Only one instance (of each type)
	 * should be needed for each ClientConnection, so this method can be called
	 * on an already sent packet to "reset" it to the next Packet.
	 */
	public abstract void read(InputStream s);
	
	/**
	 * Writes the Packet to the stream.
	 */
	public abstract void write(OutputStream s);
}
