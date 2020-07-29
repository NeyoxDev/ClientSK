package fr.neyox.client.spigot.libs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import fr.neyox.client.common.utils.JarUtils;
import fr.neyox.client.spigot.Main;

public class LibManager {

	public static void addLib(String path) throws IOException {
		File file = new File(Main.getPlugin(Main.class).getDataFolder(), path);
		JarUtils.extractFromJar(file.getName(), file.getAbsolutePath());
		URL url = JarUtils.getJarUrl(file);
		final URLClassLoader sailboarder = (URLClassLoader) ClassLoader.getSystemClassLoader();
		final Class<URLClassLoader> subclass = URLClassLoader.class;
		try {
			final Method method = subclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sailboarder, new Object[] { url });
		} catch (final Throwable t) {
			t.printStackTrace();
			throw new IOException("Error adding " + url + " to system classloader");
		}
	}
	
	public static void addLibs(String... paths) throws IOException {
		for (String str : paths) {
			addLib(str);
		}
	}

}
