package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * A task that can be executed by a goblin.
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Task extends Updateable {
	/**
	 * Returns true if the task is finished.
	 * @return True if the task is finished.
	 */
	boolean isFinished();

	/**
	 * Does the action that is represented by the task.
	 * 
	 * @param map The game map.
	 */
	void complete(GameMap map);
}
