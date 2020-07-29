package fr.neyox.client.spigot.pactify;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SchedulerUtil {
	
	private SchedulerUtil() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static void scheduleSyncDelayedTask(JavaPlugin plugin, Collection<Integer> tasksCollection, Runnable task,
			long delay) {
		(new Task(tasksCollection, task)).scheduleSyncDelayedTask(plugin, delay);
	}

	public static void cancelTasks(JavaPlugin plugin, Collection<Integer> tasksCollection) {
		for (Iterator<Integer> it = tasksCollection.iterator(); it.hasNext();) {
			plugin.getServer().getScheduler().cancelTask(((Integer) it.next()).intValue());
			it.remove();
		}
	}

	private static class Task implements Runnable {
		private final Collection<Integer> tasksCollection;

		private final Runnable handle;

		private int taskId;

		public Task(Collection<Integer> tasksCollection, Runnable handle) {
			this.tasksCollection = tasksCollection;
			this.handle = handle;
		}

		public void run() {
			this.tasksCollection.remove(Integer.valueOf(this.taskId));
			this.handle.run();
		}

		public void scheduleSyncDelayedTask(JavaPlugin plugin, long delay) {
			this.taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) plugin, this, delay);
			if (this.taskId != -1)
				this.tasksCollection.add(Integer.valueOf(this.taskId));
		}
	}
}
