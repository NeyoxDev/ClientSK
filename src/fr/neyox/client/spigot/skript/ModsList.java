package fr.neyox.client.spigot.skript;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.neyox.client.spigot.Main;

@SuppressWarnings("rawtypes")
public class ModsList extends SimpleExpression<List>{

	private Expression<Player> player;
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends List<String>> getReturnType() {
		return (Class<? extends List<String>>) List.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
		this.player = (Expression<Player>) arg0[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "get mods of";
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	protected List<String>[] get(Event arg0) {
		return Main.getPlugin(Main.class).getManager().getConfig().isForge() ? new List[] { Main.getPlugin(Main.class).getManager().getForgeManager().getMods(player.getSingle(arg0))} : new List[] {};
	}

}
