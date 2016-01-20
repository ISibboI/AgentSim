package de.isibboi.agentsim.game.entities;

/**
 * A game entity.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Entity extends Drawable, Updateable, Selectable {
	/**
	 * This entity collides with the given entity.
	 * @param tick The current tick.
	 * @param entity The other entity.
	 */
	void collideWith(Entity entity, int tick);

	/**
	 * If this entity blocks buildings, it has to be moved away before a building can be built at its location.
	 * @return True if this entity blocks buildings.
	 */
	boolean blocksBuildings();
}