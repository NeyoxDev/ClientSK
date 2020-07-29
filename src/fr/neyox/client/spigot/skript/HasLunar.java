package fr.neyox.client.spigot.skript;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.neyox.client.spigot.Main;
import fr.neyox.client.spigot.clients.ClientManager.Client;

public class HasLunar extends Condition {

	private Expression<Player> player;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean init(Expression[] arg0, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
		this.player = arg0[0];
		return true;
	}

	public String toString(@Nullable Event arg0, boolean arg1) {
		return "player have lunar";
	}

	public boolean check(Event arg0) {
		return Main.getPlugin(Main.class).getManager().getClient(player.getSingle(arg0)) == Client.LUNAR;
	}

}
