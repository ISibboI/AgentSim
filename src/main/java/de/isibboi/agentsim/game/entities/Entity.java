package de.isibboi.agentsim.game.entities;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;


/**
 * A game entity.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Entity extends Drawable {
	/**
	 * Updates this entity.
	 * 
	 * @param random The pseudo random number generator used for randomness.
	 * @throws GameUpdateException If something goes wrong.
	 */
	void update(Random random) throws GameUpdateException;
}