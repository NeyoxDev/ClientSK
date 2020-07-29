package fr.neyox.client.spigot.skript;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import fr.neyox.client.spigot.Main;

public class HasForge extends Condition {

	private Expression<Player> player;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
		this.player = (Expression<Player>) arg0[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "if player has forge";
	}

	@Override
	public boolean check(Event arg0) {
		return Main.getPlugin(Main.class).getManager().getConfig().isForge() && Main.getPlugin(Main.class).getManager().getForgeManager().useClient(player.getSingle(arg0));
	}

}
