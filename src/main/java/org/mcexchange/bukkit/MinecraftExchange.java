package org.mcexchange.bukkit;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcexchange.api.Connection;
import org.mcexchange.api.JoinLeavePacket;
import org.mcexchange.api.plugin.ExchangePlugin;
import org.mcexchange.api.plugin.ExchangePluginLoader;
import org.mcexchange.api.plugin.PluginPlugin;

public class MinecraftExchange extends JavaPlugin {
	private static MinecraftExchange instance;
	public static MinecraftExchange getInstance() { return instance; }
	private Connection sc;
	private Thread sct;
	
	public void onLoad() {
		instance = this;
	}

	private void loadPlugins() {
		System.out.println("[Minecraft Exchange] Loading plugins...");
		ExchangePluginLoader cpl = new ClientPluginLoader();
		List<ExchangePlugin> plugins =  cpl.loadAllPlugins();
		for(ExchangePlugin ep : plugins) {
			Class<?>[] interfaces = ep.getClass().getInterfaces();
			for(Class<?> i : interfaces) {
				if(!ExchangePlugin.class.isAssignableFrom(i)) continue;
				if(ExchangePlugin.plugins.containsKey(i)) {
					ExchangePlugin.plugins.get(i).add(ep);
				} else {
					List<ExchangePlugin> lep = new ArrayList<ExchangePlugin>();
					lep.add(ep);
					Class<? extends ExchangePlugin> clazz = i.asSubclass(ExchangePlugin.class);
					ExchangePlugin.plugins.put(clazz, lep);
				}
			}
		}
		//register PluginPlugins
		List<ExchangePlugin> pluginplugins = ExchangePlugin.plugins.get(PluginPlugin.class);
		if(pluginplugins==null) return;
		for(ExchangePlugin ep : pluginplugins) {
			PluginPlugin pp = ((PluginPlugin) ep);
			Class<? extends ExchangePlugin>[] clazz  = pp.getPlugins();
			if(clazz == null) continue;
			for(Class<? extends ExchangePlugin> cp : clazz) {
				List<ExchangePlugin> cps = ExchangePlugin.plugins.get(cp);
				if(cps == null) continue;
				for(ExchangePlugin exp : cps) pp.registerPlugin(exp);
			}
		}
		System.out.println("[Minecraft Exchange] Plugins loaded.");
	}
	
	private void connect() {
		System.out.println("[Minecraft Exchange] Connecting to the server...");
		File f = new File(getDataFolder(), "config.yml");
		if(!f.exists()) saveDefaultConfig();
		try{
			InetSocketAddress host = new InetSocketAddress(getConfig().getString("host", "localhost"), getConfig().getInt("port",62924));
			SocketChannel channel = SocketChannel.open(host);
			sc = new Connection(channel);
			sct = new Thread(sc);
			sct.start();
		} catch(UnknownHostException e) {
			System.err.println("[Minecraft Exchange] Could not connect to host.");
			e.printStackTrace();
			disable();
			return;
		} catch(IOException ioe) {
			System.err.println("[Minecraft Exchange] Could not get I/O for the connection.");
			ioe.printStackTrace();
			disable();
			return;
		}
		System.out.println("[Minecraft Exchange] Connected to the server.");
	}
	
	public void onEnable() {
		loadPlugins();
		connect();
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
