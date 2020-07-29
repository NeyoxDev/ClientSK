package fr.neyox.client.spigot.utils;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Util {

	public static int readVarInt(ObjectInputStream ois) {
		try {
			return ois.readInt();
		} catch (IOException e) {
			return 0;
		}
	}

}
