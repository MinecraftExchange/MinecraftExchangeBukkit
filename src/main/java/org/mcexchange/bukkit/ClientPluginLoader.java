package org.mcexchange.bukkit;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;
import org.mcexchange.api.plugin.ExchangePluginLoader;
import org.mcexchange.api.plugin.MCPluginExchangePlugin;

/**
 * A subclass of ExchangePluginLoader that provides functionality for MCPluginExchangePlugins.
 */
public class ClientPluginLoader extends ExchangePluginLoader {
	@Override
	public Object instantiate(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		if(!MCPluginExchangePlugin.class.isAssignableFrom(clazz)) return super.instantiate(clazz);
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		for(Plugin p : plugins) {
			if(clazz.isInstance(p)) {
				if(!p.isEnabled()) p.getPluginLoader().enablePlugin(p);
				return p;
			}
		}
		File jar = null;
		try {
			String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			jar = new File(decodedPath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return super.instantiate(clazz);
		}
		Plugin p = null;
		try {
			p = Bukkit.getPluginManager().getPlugins()[0].getPluginLoader().loadPlugin(jar);
		} catch (InvalidPluginException e) {
			e.printStackTrace();
			return super.instantiate(clazz);
		} catch (InvalidDescriptionException e) {
			e.printStackTrace();
			return super.instantiate(clazz);
		} catch (UnknownDependencyException e) {
			e.printStackTrace();
			return super.instantiate(clazz);
		}
		p.getPluginLoader().enablePlugin(p);
		if(clazz.isInstance(p)) return p;
		System.out.println(p);
		return super.instantiate(clazz);
	}
}
