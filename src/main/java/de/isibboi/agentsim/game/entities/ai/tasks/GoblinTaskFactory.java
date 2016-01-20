/**
 * 
 */
package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import de.isibboi.agentsim.algorithm.BlockadeMap;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.entities.Goblin;
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
	 * @param goblin The goblin that should mine a block.
	 * @param entityLocationManager The entity location manager.
	 * @return A mining task.
	 */
	public Iterable<? extends Task> createMiningTask(final Point location, final Goblin goblin, final EntityLocationManager entityLocationManager) {
		Queue<Task> _taskQueue = new LinkedList<>();
		LockLocationTask lockLocationTask = new LockLocationTask(entityLocationManager.getMap(), location, goblin);

		_taskQueue.add(lockLocationTask);
		_taskQueue.add(new MiningTask(location, goblin, entityLocationManager));
		_taskQueue.add(new UnlockLocationTask(lockLocationTask));

		return _taskQueue;
	}

	/**
	 * Creates a {@link MoveToTask}.
	 * 
	 * @param target The target to move to.
	 * @param goblin The goblin that should move to the target.
	 * @return A task that moves the given goblin to the given target.
	 */
	public Iterable<? extends Task> createMoveToTask(final Point target, final Goblin goblin) {
		BlockadeMap blockadeMap = goblin.getAI().getBlockadeMap();

		return Collections.singleton(new MoveToTask(target, goblin, blockadeMap));
	}
}
