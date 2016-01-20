package de.isibboi.agentsim.game.entities.ai.tasks;

import de.isibboi.agentsim.game.entities.Movement;

/**
 * Handles priorities.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class AbstractTask implements Task {
	private Movement _movement;

	@Override
	public void zeroTimeAction() {
		// Ignore.
	}

	@Override
	public Movement getMovement() {
		return _movement;
	}

	/**
	 * Sets the movement to be returned by {@code #getMovement()}.
	 * @param movement The movement.
	 */
	protected void setMovement(final Movement movement) {
		_movement = movement;
	}
}