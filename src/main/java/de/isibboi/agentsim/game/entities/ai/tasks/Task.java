package de.isibboi.agentsim.game.entities.ai.tasks;

import de.isibboi.agentsim.algorithm.PriorityOrdered;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.entities.Updateable;

/**
 * A task that can be executed by an entity.
 * The task has to be static after creation.
 * That means, the course of the task must not change after creation.
 * The AI is the only one that decides about what is being done.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Task extends Updateable, PriorityOrdered {
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
	 * Called when there is new information available that could make the task impossible.
	 * No task optimizations should happen here, the task is static after creation.
	 */
	void eventInformationUpdated();

	/**
	 * Only to be used by {@link ZeroTimeTask}s.
	 * Must define if the task was completed successful.
	 */
	void zeroTimeAction();

	// TODO Move the following methods to planned task.
	/**
	 * Returns the priority of this task.
	 * The higher the priority, the earlier the task will be executed.
	 * @return The priority.
	 */
	int getPriority();

	/**
	 * Sets the priority of this task.
	 * 
	 * @param priority The priority, must be greater than or equal to zero.
	 */
	void setPriority(int priority);
}