package fr.neyox.client.spigot;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import fr.neyox.client.spigot.clients.ClientManager;
import fr.neyox.client.spigot.config.Config;
import fr.neyox.client.spigot.config.ConfigManager;
import fr.neyox.client.spigot.date.NowDateFormat;
import fr.neyox.client.spigot.libs.LibManager;
import fr.neyox.client.spigot.skript.HasForge;
import fr.neyox.client.spigot.skript.HasLabyMod;
import fr.neyox.client.spigot.skript.HasLunar;
import fr.neyox.client.spigot.skript.HasPactify;
import fr.neyox.client.spigot.skript.ModsList;
import fr.neyox.client.spigot.version.VersionManager;
import fr.neyox.client.spigot.version.VersionPlayer;
import lombok.Getter;

@Getter
public class Main extends JavaPlugin {
	
	private Config pluginConfig;
	
	public static final Gson GSON = new GsonBuilder().serializeNulls().create();
	
	private ClientManager manager;
	
	private ConfigManager configManager;
	
	@Override
	public void onEnable() {
		if (!System.getProperty("java.version").contains("1.8")) {
			Bukkit.getLogger().warning("Il faut avoir java 8 pour utiliser ce plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		loadLibs();
		loadConfig();
		this.manager = new ClientManager(this.pluginConfig);
		Skript.registerAddon(this);
		Skript.registerCondition(HasPactify.class, "[ClientSK] %player% (have|has) [the] pactify[launcher]");
		Skript.registerCondition(HasLunar.class, "[ClientSK] %player% (have|has) [the] lunar[client]");
		Skript.registerCondition(HasLabyMod.class, "[ClientSK] %player% (have|has) [the] laby[mod]");
		if (getServer().getPluginManager().isPluginEnabled("ViaVersion")) {
			VersionManager.useViaVersion = true;
		}else {
			new VersionManager();
		}
		Skript.registerExpression(VersionPlayer.class, String.class, ExpressionType.PROPERTY, "[ClientSK] version launcher of %player%");
		Skript.registerExpression(NowDateFormat.class, String.class, ExpressionType.PROPERTY, "[ClientSK] format of %date%");
		Skript.registerCondition(HasForge.class, "[ClientSK] %player% (have|has) forge");
		Skript.registerExpression(ModsList.class, List.class, ExpressionType.PROPERTY, "[ClientSK] get forge mods of %player%");
	}

	private void loadConfig() {
		this.configManager = new ConfigManager(this);
		this.pluginConfig = configManager.getPluginConfig();
	}

	private void loadLibs() {
		Bukkit.getLogger().info("Chargement des libraries...");
		try {
			saveResources("lombok.jar", "PactifyProtocol.jar");
			LibManager.addLibs("lombok.jar", "PactifyProtocol.jar");
		} catch (Exception e) {
			Bukkit.getLogger().warning("Erreur lors des chargements des libraries");
			e.printStackTrace();
			return;
		}
		Bukkit.getLogger().info("Libraries chargés !");
	}

	private void saveResources(String... strings) {
		for (String strr : strings) {
			saveResource(strr, true);
		}
	}

	public ClassLoader getClassL() {
		return getClassLoader();
	}

}
