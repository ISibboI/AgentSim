package de.isibboi.agentsim.game.entities.ai.tasks;

import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.entities.Updateable;

/**
 * A task that can be executed by an entity.
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
	 * Returns if the execution of this task was successful.
	 * @return True if the execution of this task was successful.
	 */
	boolean wasSuccessful();

	/**
	 * Returns the movement order for the current step.
	 * @return The movement order.
	 */
	Movement getMovement();

	/**
	 * Guesses the duration of this task. This should be as accurate as possible, and never lower than the actual duration. Otherwise entities might starve.
	 * @return The duration of this task.
	 */
	int guessDuration();

	/**
	 * Called when the task tries to do something that cannot be done in the world.
	 */
	void eventFailure();

	/**
	 * Called when there is new information available that could make the task faster.
	 */
	void eventInformationUpdated();

	/**
	 * Starts the task.
	 * This method should do some preparations that have to be done just once.
	 * After return of this method, the first movement should be ready.
	 */
	void start();
}