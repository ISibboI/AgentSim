package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.entities.Updateable;

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
}
