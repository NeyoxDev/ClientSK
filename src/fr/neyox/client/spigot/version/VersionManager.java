package fr.neyox.client.spigot.version;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.neyox.client.common.utils.ProtocolLib;
import fr.neyox.client.common.utils.Reflection;
import fr.neyox.client.spigot.Main;
import io.netty.channel.Channel;
import us.myles.ViaVersion.api.Via;

public class VersionManager implements Listener {
	
	public static boolean useViaVersion;
	
	private static Map<Channel, String> protocol = new HashMap<>();
	
	public static String getVersion(Player player) {
		if (useViaVersion) {
			return ProtocolVersion.getProtocol(Via.getAPI().getPlayerVersion(player.getUniqueId())).getName();
		}else {
			try {
				return protocol.get(getChannel(player));
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	private static Channel getChannel(Player player) throws NoSuchFieldException, IllegalAccessException {
		return (Channel) Reflection.getValue(Reflection.getValue(Reflection.getValue(Reflection.getHandle(player), "playerConnection"), "networkManager"), "channel");
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		try {
			if (protocol.containsKey(getChannel(e.getPlayer()))) {
				protocol.remove(getChannel(e.getPlayer()));
			}
		} catch (Exception e22) {}
	}
	
	public VersionManager() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin(Main.class));
		new ProtocolLib(Main.getPlugin(Main.class)) {
			@Override
			public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
				if (packet.getClass().equals(Reflection.getNMSClass("PacketHandshakingInSetProtocol"))) {
					try {
						protocol.put(channel, ProtocolVersion.getProtocol(Reflection.getField(packet.getClass(), true, "a").getInt(packet)).getName());
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
				}
				return super.onPacketInAsync(sender, channel, packet);
			}
		};
	}
}
