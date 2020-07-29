package fr.neyox.client.spigot.labymod;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Sets;

import fr.neyox.client.spigot.Main;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import lombok.Getter;

@Getter
public class LabyModManager implements Listener {

	private Main plugin;

	private Set<UUID> usedsClients;

	public LabyModManager(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getLogger().info(this.getClass().getSimpleName()+" class loaded");
		this.usedsClients = Sets.newHashSet();
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "LABYMOD", new PluginMessageListener() {
			@Override
			public void onPluginMessageReceived(String channel, final Player player, byte[] bytes) {
				// Converting the byte array into a byte buffer
				ByteBuf buf = Unpooled.wrappedBuffer(bytes);

				try {
					// Reading the version from the buffer
					final String version = readString(buf, Short.MAX_VALUE);

					// Calling the event synchronously
					Bukkit.getScheduler().runTask(plugin, new Runnable() {
						@Override
						public void run() {
							// Checking whether the player is still online
							if (!player.isOnline())
								return;

							usedsClients.add(player.getUniqueId());
						}
					});
				} catch (RuntimeException ex) {
				}
			}
		});
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		if (usedsClients.contains(e.getPlayer().getUniqueId())) {
			usedsClients.remove(e.getPlayer().getUniqueId());
		}
	}

	public String readString(ByteBuf buf, int maxLength) {
		int i = this.readVarIntFromBuffer(buf);

		if (i > maxLength * 4) {
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i
					+ " > " + maxLength * 4 + ")");
		} else if (i < 0) {
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		} else {
			byte[] bytes = new byte[i];
			buf.readBytes(bytes);

			String s = new String(bytes, Charset.forName("UTF-8"));
			if (s.length() > maxLength) {
				throw new DecoderException(
						"The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
			} else {
				return s;
			}
		}
	}

	public int readVarIntFromBuffer(ByteBuf buf) {
		int i = 0;
		int j = 0;

		byte b0;
		do {
			b0 = buf.readByte();
			i |= (b0 & 127) << j++ * 7;
			if (j > 5) {
				throw new RuntimeException("VarInt too big");
			}
		} while ((b0 & 128) == 128);

		return i;
	}

	public boolean hasClient(Player player) {
		return usedsClients.contains(player.getUniqueId());
	}

}
