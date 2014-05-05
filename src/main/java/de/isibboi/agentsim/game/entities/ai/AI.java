package de.isibboi.agentsim.game.entities.ai;

import java.awt.Point;

import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.Goblin;

/**
 * An interface for AI systems. Specifies all the events that can be fired by a goblin.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface AI {
	/**
	 * If the goblin tries to walk into a certain direction, but there is a wall, then this event is fired.
	 * The point is the point of the wall that the goblin tried to walk on.
	 * 
	 * @param location The location at which the collision happened.
	 */
	void eventCollideWithWall(Point location);

	/**
	 * If the goblins steps on the location of another goblin, this event is fired.
	 * @param entity The other goblin.
	 */
	void eventCollideWithEntity(Goblin goblin);

	/**
	 * If the goblin walks onto the given location, this event is fired.
	 * 
	 * @param location The location to goblin walked onto.
	 */
	void eventMoveTo(Point location);

	/**
	 * If the goblin finishes a task, this event is fired.
	 */
	void eventTaskFinished();

	/**
	 * If the goblin accepts the task returned by {@link #getNewTask()}, this event is fired.
	 */
	void eventTaskAccepted();

	/**
	 * After each update, if the goblin has no task, it checks if the AI has a new task.
	 * If this method returns null, the goblin will just continue moving, otherwise it will execute the new task.
	 * 
	 * @return The new task for the goblin, or null if it should continue moving.
	 */
	Task getNewTask();
}
