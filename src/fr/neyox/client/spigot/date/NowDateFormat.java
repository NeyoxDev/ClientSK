package fr.neyox.client.spigot.date;

import java.text.SimpleDateFormat;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;

public class NowDateFormat extends SimpleExpression<String> {

	private Expression<Date> date;
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, ParseResult arg3) {
		this.date = (Expression<Date>) arg0[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "get current date";
	}

	@Override
	@Nullable
	protected String[] get(Event arg0) {
		return new String[] {new SimpleDateFormat("EEEE dd MMMM YYYY � HH:mm").format(date.getSingle(arg0).getTimestamp())};
	}

}
