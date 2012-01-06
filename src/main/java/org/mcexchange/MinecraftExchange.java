package org.mcexchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftExchange extends JavaPlugin {
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	public void onDisable() {
		System.out.println("[Minecraft Exchange] is disconnecting from the server.");
		out.close();
		try {
			in.close();
		} catch (IOException e) {
			System.out.println("[Minecraft Exchange] could not close input stream.");
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("[Minecraft Exchange] could not close connection.");
			e.printStackTrace();
		}
		System.out.println("[Minecraft Exchange] disabled.");
	}

	public void onEnable() {
		System.out.println("[Minecraft Exchange] is connecting to the server.");
		
		try{
			//localhost just for testing purposes
			socket = new Socket("localhost", 62924);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch(UnknownHostException e) {
			System.out.println("[Minecraft Exchange] could not connect to host.");
			e.printStackTrace();
			disable();
		}
		catch(IOException ioe) {
			System.out.println("[Minecraft Exchange] could not get I/O for the connection.");
			ioe.printStackTrace();
			disable();
		}
		
		System.out.println("[Minecraft Exchange] has connected to the server.");
			
		System.out.println("[Minecraft Exchange] enabled.");
	}
	
	public void disable() {
		this.setEnabled(false);
	}

}
