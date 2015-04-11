package de.isibboi.agentsim.game.entities;

/**
 * A game entity.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Entity extends Drawable, Updateable {
	/**
	 * This entity collides with the given entity.
	 * @param entity The other entity.
	 */
	void collideWith(Entity entity);
}