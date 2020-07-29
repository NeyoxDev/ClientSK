package fr.neyox.client.spigot.clients;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

import fr.neyox.client.spigot.Main;
import fr.neyox.client.spigot.badlion.BadlionManager;
import fr.neyox.client.spigot.config.Config;
import fr.neyox.client.spigot.forge.ForgeManager;
import fr.neyox.client.spigot.labymod.LabyModManager;
import fr.neyox.client.spigot.lunar.LunarManager;
import fr.neyox.client.spigot.pactify.PactifyManager;
import lombok.Getter;

@Getter
public class ClientManager {
	
	private Config config;
	
	private PactifyManager pactifyManager;
	
	private LunarManager lunarManager;
	
	private LabyModManager labyModManager;
	
	private BadlionManager badionManager;
	
	private ForgeManager forgeManager;
	
	public ClientManager(Config config) {
		Main main = Main.getPlugin(Main.class);
		this.config = config;
		if (config.isPactify()) {
			this.pactifyManager = new PactifyManager(main);
			if (SpigotConfig.bungee) {
				Bukkit.getLogger().info("Pensez à rejouter le plugin dans votre proxy si ce n'est pas déjà fait.");
			}
		}
		if (config.isLunar()) {
			this.lunarManager = new LunarManager(main);
		}
		if (config.isLabymod()) {
			this.labyModManager = new LabyModManager(main);
		}
		if (!config.getDisabledBadlioMods().isEmpty()) {
			this.badionManager = new BadlionManager(main);
		}
		if (config.isForge()) {
			if (!SpigotConfig.bungee) {
				Bukkit.getLogger().info("Il faut que votre serveur soit relier à un proxy pour utiliser le ForgeManager");
				return;
			}
			this.forgeManager = new ForgeManager();
		}
		
	}
	
	public Client getClient(Player player) {
		if (config.isLabymod() && getLabyModManager().hasClient(player)) {
			return Client.LABYMOD;
		}
		if (config.isLunar() && getLunarManager().useClient(player)) {
			return Client.LUNAR;
		}
		if (config.isPactify() && getPactifyManager().useClient(player)) {
			return Client.PACTIFY;
		}
		if (config.isForge() && getForgeManager().useClient(player))
		{
			return Client.FORGE;
		}
		return Client.MINECRAFT;
	}
	
	
	public static enum Client {
		
		LUNAR,
		PACTIFY,
		LABYMOD,
		MINECRAFT,
		FORGE;
		
	}
	
}
