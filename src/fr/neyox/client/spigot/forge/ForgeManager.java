package fr.neyox.client.spigot.forge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.neyox.client.spigot.Main;

public class ForgeManager implements Listener, PluginMessageListener {

	private Main main = Main.getPlugin(Main.class);

	private List<Player> players;
	
	private Map<Player, ForgeMod> mods;

	public ForgeManager() {
		this.players = new ArrayList<Player>();
		this.mods = new HashMap<Player, ForgeMod>();
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin(Main.class));
		Bukkit.getMessenger().registerOutgoingPluginChannel(main, "ClientSK");
		Bukkit.getMessenger().registerIncomingPluginChannel(main, "ClientSK", this);
		Bukkit.getLogger().info(this.getClass().getSimpleName() + " class loaded");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws IOException {
		new BukkitRunnable() {
			@Override
			public void run() {
				FMLHandshake(e);
			}

		}.runTaskLaterAsynchronously(main, 5);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		players.remove(e.getPlayer());
		mods.remove(e.getPlayer());
	}
	
	protected void FMLHandshake(PlayerJoinEvent e) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forge");
		out.writeUTF(e.getPlayer().getName());
		e.getPlayer().sendPluginMessage(main, "ClientSK", out.toByteArray());
	}

	public boolean useClient(Player player) {
		return this.players.contains(player);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("ClientSK"))
			return;
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		if (in.readUTF().equals("Forge")) {
			if (in.readBoolean()) {
				mods.put(player, Main.GSON.fromJson(in.readLine(), ForgeMod.class));
				players.add(player);
			}
		}
	}

	public List<String> getMods(Player single) {
		return mods.get(single).getMods();
	}

}
