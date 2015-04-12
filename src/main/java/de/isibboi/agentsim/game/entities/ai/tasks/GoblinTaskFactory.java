/**
 * 
 */
package de.isibboi.agentsim.game.entities.ai.tasks;

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
}
