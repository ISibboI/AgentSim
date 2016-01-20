package de.isibboi.agentsim.game;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entity;

/**
 * A single threaded {@link EntityCollider} that does no optimizations.  
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class SimpleEntityCollider implements EntityCollider {
	private int _currentTick;

	/**
	 * @param settings Ignored.
	 */
	public SimpleEntityCollider(final Settings settings) {
		// Do nothing
	}

	@Override
	public void startCollision(final int tick) {
		_currentTick = tick;
	}

	@Override
	public void collide(final Entity a, final Entity b) {
		a.collideWith(b, _currentTick);
	}

	@Override
	public void finishBlock() {
		// Do nothing.
	}

	@Override
	public void finishCollision() {
		// Do nothing.
	}

	@Override
	public void shutdown() {
		// Do nothing.
	}
}