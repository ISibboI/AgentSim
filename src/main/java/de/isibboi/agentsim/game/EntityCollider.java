package de.isibboi.agentsim.game;

import de.isibboi.agentsim.game.entities.Entity;

/**
 * Handles the entity collisions that are found.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public interface EntityCollider {
	/**
	 * Starts the collision process.
	 * After calling this, the {@link EntityCollider} is ready to accept calls to {@link #collide(Entity, Entity)}.
	 */
	void startCollision();

	/**
	 * Calculates the collision between the given entities.
	 * It is not guaranteed that the evaluation has finished after this method returns.
	 * 
	 * @param a The first entity.
	 * @param b The second entity.
	 */
	void collide(Entity a, Entity b);

	/**
	 * When this method is called, the caller guarantees that no entity that has been passed to {@link #collide(Entity, Entity)} before will be passed again.
	 */
	void finishBlock();

	/**
	 * Finished the collision calculation for all entities.
	 * When this method returns, all collisions that were passed to {@link #collide(Entity, Entity)} are evaluated.
	 */
	void finishCollision();

	/**
	 * Releases all additional resources allocated by this object.
	 * After calling this method, the object might not be usable anymore.
	 */
	void shutdown();
}
