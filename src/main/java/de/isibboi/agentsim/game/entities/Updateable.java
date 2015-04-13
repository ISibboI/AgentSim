package de.isibboi.agentsim.game.entities;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;

/**
 * Classes implementing this can be updated by the game tick.
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Updateable {

	/**
	 * Updates this entity. This method must be called once per tick.
	 * 
	 * @param random The pseudo random number generator used for randomness.
	 * @param tick The tick number of this update.
	 * @throws GameUpdateException If something goes wrong.
	 */
	void update(Random random, int tick) throws GameUpdateException;
}
