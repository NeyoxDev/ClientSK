package fr.neyox.client.spigot.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import fr.neyox.client.common.utils.IOUtil;
import fr.neyox.client.spigot.Main;
import lombok.Getter;

public class ConfigManager {
	
	@Getter
	private Config pluginConfig;
	
	@SuppressWarnings("resource")
	public ConfigManager(Main main) {
		this.pluginConfig = main.getPluginConfig();
		Bukkit.getLogger().info("Chargement de la confiuration...");
		try {
			File configFile = new File(main.getDataFolder(), "/config.json");
			if (!configFile.exists()) {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
				this.pluginConfig = new Config(true, true, true, true, new ArrayList<String>());
				IOUtil.writeFromInputStream(new FileOutputStream(configFile), Main.GSON.toJson(pluginConfig));
				return;
			}
			try {
				this.pluginConfig = Main.GSON.fromJson(IOUtil.readInputStreamAsString(new FileInputStream(configFile)), Config.class);
				this.pluginConfig.init();
			} catch (Exception e) {
				this.pluginConfig = new Config(true, true, true, true, new ArrayList<String>());
			}
			
			if (pluginConfig == null) {
				this.pluginConfig = new Config(true, true, true, true, new ArrayList<String>());
			}
			
		} catch (Exception e) {
			Bukkit.getLogger().warning("Erreur lors des chargements de la configuration");
			e.printStackTrace();
			return;
		}
		Bukkit.getLogger().info("Configuration chargée (" + pluginConfig.isLabymod() +";" + pluginConfig.isLunar() +";" + pluginConfig.isPactify()+"; "+pluginConfig.isForge()+")");
	}

}
