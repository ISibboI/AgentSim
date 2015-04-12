/**
 * 
 */
package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.LinkedList;
import java.util.Queue;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.map.Point;

/**
 * Creates macroscopic tasks for the goblin AI.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class GoblinTaskFactory {
	/**
	 * Creates the task that should be executed during idling.
	 * @return The idle task.
	 */
	public Task createIdleTask() {
		return new RandomMovementTask();
	}

	/**
	 * Creates a task that mines a block.
	 * 
	 * @param location The location that should be mined.
	 * @param entity The entity that should mine a block.
	 * @param entityLocationManager The entity location manager.
	 * @return A mining task.
	 */
	public Task createMiningTask(final Point location, final Entity entity, final EntityLocationManager entityLocationManager) {
		Queue<Task> _taskQueue = new LinkedList<>();

		_taskQueue.add(new LockLocationTask(entityLocationManager.getMap(), location));
		_taskQueue.add(new MiningTask(location, entity, entityLocationManager));
		_taskQueue.add(new UnlockLocationTask(entityLocationManager.getMap(), location));

		return new CompositeTask(_taskQueue);
	}
}
