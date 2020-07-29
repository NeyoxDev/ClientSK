package fr.neyox.client.spigot.lunar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;

import fr.neyox.client.spigot.Main;
import lombok.Getter;

@Getter
public class LunarManager implements Listener {

	private Main plugin;

	private UserManager userManager;

	private LunarAPI api;

	public LunarManager(Main main) {
		this.plugin = main;
		this.api = new CraftLunerAPI(this);
		this.userManager = new UserManager();
		Bukkit.getLogger().info(this.getClass().getSimpleName()+" class loaded");
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "Lunar-Client");
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "Lunar-Client",
				(channel, player, bytes) -> {
					if (bytes[0] == 26) {
						final UUID outcoming = BufferUtils.getUUIDFromBytes(Arrays.copyOfRange(bytes, 1, 30));

						// To stop server wide spoofing.
						if (!outcoming.equals(player.getUniqueId())) {
							return;
						}

						User user = getApi().getUserManager().getPlayerData(player);

						if (user != null && !user.isLunarClient()) {
							user.setLunarClient(true);
						}

						for (Player other : Bukkit.getOnlinePlayers()) {
							if (getApi().isAuthenticated(other)) {
								other.sendPluginMessage(plugin, channel, bytes);
							}
						}
					}
				});
	}

	@EventHandler
	public void onRegisterChannel(PlayerRegisterChannelEvent event) {
		if (event.getChannel().equals("Lunar-Client")) {
			try {
				this.getApi().performEmote(event.getPlayer(), 5, false);
				this.getApi().performEmote(event.getPlayer(), -1, false);

				Object nmsPlayer = event.getPlayer().getClass().getMethod("getHandle").invoke(event.getPlayer());
				Object packet = ReflectionUtils.getNmsClass("PacketPlayOutEntityStatus")
						.getConstructor(ReflectionUtils.getNmsClass("Entity"), byte.class)
						.newInstance(nmsPlayer, (byte) 2);
				ReflectionUtils.sendPacket(event.getPlayer(), packet);
			} catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException
					| NoSuchFieldException | ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		User data = new User(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        getApi().getUserManager().setPlayerData(event.getPlayer().getUniqueId(), data);
	}
	
	@EventHandler
    public void onQuit(PlayerQuitEvent event) {
        getApi().getUserManager().removePlayerData(event.getPlayer().getUniqueId());
    }
	
	@EventHandler
	public void onUnregisterChannel(PlayerUnregisterChannelEvent event) {
		User user = this.getApi().getUserManager().getPlayerData(event.getPlayer());
		if (event.getChannel().equals("Lunar-Client")) {
			user.setLunarClient(false);
		}
	}
	
	public boolean useClient(Player player) {
		return getUserManager().getPlayerData(player).isLunarClient();
	}

}
