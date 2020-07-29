package fr.neyox.client.spigot.pactify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import fr.neyox.client.spigot.Main;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import pactify.client.api.plsp.packet.client.PLSPPacketConfFlag;
import pactify.client.api.plsp.packet.client.PLSPPacketConfFlags;
import pactify.client.api.plsp.packet.client.PLSPPacketReset;

@RequiredArgsConstructor
@Data
public class PactifyPlayer {

	private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern
			.compile("[\u0000\u0002]PAC([0-9A-F]{5})[\u0000\u0002]");

	private final PactifyManager service;
	private final Player player;
	private final Set<Integer> scheduledTasks = new HashSet<>();
	private boolean joined;
	private int launcherProtocolVersion;

	public void init() {
		List<MetadataValue> hostnameMeta = player.getMetadata("ClientSK:hostname");
		if (!hostnameMeta.isEmpty()) {
			String hostname = hostnameMeta.get(0).asString();
			Matcher m = PACTIFY_HOSTNAME_PATTERN.matcher(hostname);
			if (m.find()) {
				launcherProtocolVersion = Math.max(1, Integer.parseInt(m.group(1), 16));
			}
		} else {
			service.getPlugin().getLogger().warning("Unable to verify the launcher of " + player.getName()
					+ ": it probably logged when the plugin was disabled!");
		}

		BukkitUtil.addChannel(player, "PLSP"); // Register the PLSP channel for the Player.sendPluginMessage calls
	}

	public void join() {
		joined = true;

		// This client can come from another server if BungeeCord is used, so we reset
		// it to ensure a clean state!
		service.sendPLSPMessage(player, new PLSPPacketReset());

		// Send client capabilities
		// TODO: Add config and API
		boolean attackCooldown = false;
		boolean playerPush = false;
		boolean largeHitbox = true;
		boolean swordBlocking = true;
		boolean hitAndBlock = true;
		service.sendPLSPMessage(player,
				new PLSPPacketConfFlags(Arrays.asList(new PLSPPacketConfFlag("attack_cooldown", attackCooldown),
						new PLSPPacketConfFlag("player_push", playerPush),
						new PLSPPacketConfFlag("large_hitbox", largeHitbox),
						new PLSPPacketConfFlag("sword_blocking", swordBlocking),
						new PLSPPacketConfFlag("hit_and_block", hitAndBlock))));
	}

	public void free(boolean onQuit) {
		SchedulerUtil.cancelTasks(Main.getPlugin(Main.class), scheduledTasks);
		if (!onQuit) {
			service.sendPLSPMessage(player, new PLSPPacketReset());
		}
	}

	public boolean hasLauncher() {
		return launcherProtocolVersion > 0;
	}

	public int getLauncherProtocolVersion() {
		return launcherProtocolVersion;
	}

	
}
