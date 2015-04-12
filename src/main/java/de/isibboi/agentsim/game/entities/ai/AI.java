package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.entities.Attributes;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.map.Point;

/**
 * An interface for AI systems. Specifies all the events that can be fired by an entity.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface AI {
	/**
	 * If the entity tries to walk into a certain direction, but there is a wall, then this event is fired.
	 * The point is the point of the wall that the entity tried to walk on.
	 * 
	 * @param location The location at which the collision happened.
	 */
	void eventCollideWithWall(Point location);

	/**
	 * If the entity steps on the location of another entity, this event is fired.
	 * @param entity The other entity.
	 */
	void eventCollideWithEntity(Entity entity);

	/**
	 * If the entity tries to walk out of the map, this event is fired.
	 * @param location The location at which the collision happened.
	 */
	void eventCollideWithMapBorder(Point location);

	/**
	 * If the entity walks onto the given location, this event is fired.
	 * 
	 * @param location The location to entity walked onto.
	 */
	void eventMoveTo(Point location);

	/**
	 * Updates the AI.
	 * @param attributes The current attributes of the controlled entity.
	 */
	void update(Attributes attributes);

	/**
	 * Returns true if the entity is alive and should continue updating, false if it is dead.
	 * @return True if the entity is alive.
	 */
	boolean isAlive();

	/**
	 * Returns the next movement of the entity.
	 * @return The next movement of the entity.
	 */
	Point getMovement();
}
