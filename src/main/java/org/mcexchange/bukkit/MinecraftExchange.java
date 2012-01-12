package org.mcexchange.bukkit;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcexchange.api.Connection;
import org.mcexchange.api.JoinLeavePacket;

public class MinecraftExchange extends JavaPlugin {
	private static MinecraftExchange instance;
	public static MinecraftExchange getInstance() { return instance; }
	private Connection sc;
	private Thread sct;
	
	public void onLoad() {
		instance = this;
	}

	public void onEnable() {
		System.out.println("[Minecraft Exchange] is connecting to the server.");
		File f = new File(getDataFolder(), "config.yml");
		if(!f.exists()) saveDefaultConfig();
		try{
			//localhost just for testing purposes
			InetSocketAddress host = new InetSocketAddress(getConfig().getString("host", "localhost"), getConfig().getInt("port",62924));
			SocketChannel channel = SocketChannel.open(host);
			sc = new Connection(channel);
			sct = new Thread(sc);
			sct.start();
		} catch(UnknownHostException e) {
			System.err.println("[Minecraft Exchange] Could not connect to host.");
			e.printStackTrace();
			disable();
		} catch(IOException ioe) {
			System.err.println("[Minecraft Exchange] could not get I/O for the connection.");
			ioe.printStackTrace();
			disable();
		}
		
		System.out.println("[Minecraft Exchange] has connected to the server.");
			
		System.out.println("[Minecraft Exchange] enabled.");
	}

	public void onDisable() {
		sct.interrupt();
		System.out.println("[Minecraft Exchange] disabled.");
	}
	
	public void disable() {
		this.setEnabled(false);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			return false;
		} else {
			if(cmd.getName().equalsIgnoreCase("exchange")) {
				if(args[0].equalsIgnoreCase("join")) {
					JoinLeavePacket jlp = new JoinLeavePacket(sc);
					jlp.setType(true);
					jlp.setNetwork(args[1]);
					sc.sendPacket(jlp);
				}
			}
		}
		
		return false;
	}

}
