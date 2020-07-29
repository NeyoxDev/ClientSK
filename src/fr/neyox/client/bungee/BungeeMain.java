package fr.neyox.client.bungee;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;

import fr.neyox.client.spigot.forge.ForgeMod;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;

public class BungeeMain extends Plugin implements Listener {

	private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern.compile("\000(PAC[0-9A-F]{5})\000");

	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().registerChannel("ClientSK");
	}

	@EventHandler
	public void onPlayerHandshake(PlayerHandshakeEvent event) {
		InitialHandler con = (InitialHandler) event.getConnection();
		Matcher m = PACTIFY_HOSTNAME_PATTERN.matcher(con.getExtraDataInHandshake());
		if (m.find())
			event.getHandshake().setHost(String.valueOf(event.getHandshake().getHost()) + "\002" + m.group(1) + "\002");
	}
	
	@EventHandler
	public void onMessage(PluginMessageEvent e) {
		if (e.getTag().equals("ClientSK")) {
			ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
			if (in.readUTF().equals("Forge")) {
				ProxiedPlayer player = getProxy().getPlayer(in.readUTF());
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeBoolean(player.isForgeUser());
				if (player.isForgeUser()) {
					@SuppressWarnings("serial")
					ForgeMod mod = new ForgeMod(new ArrayList<String>() {
						{
							for (Entry<String, String> mods : player.getModList().entrySet()) {
								this.add(mods.getValue());
							}
						}
					});
					out.writeUTF(new Gson().toJson(mod));
					player.getServer().sendData("ClientSK", out.toByteArray());
				}
			}
		}
	}
}
