package fr.neyox.client.spigot.config;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Config {

	private boolean pactify, lunar, labymod, forge;
	
	private List<String> disabledBadlioMods;

	public void init() {
		if (disabledBadlioMods == null) disabledBadlioMods = new ArrayList<String>();
	}
	
}
