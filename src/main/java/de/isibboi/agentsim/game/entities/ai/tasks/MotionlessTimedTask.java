package de.isibboi.agentsim.game.entities.ai.tasks;

import de.isibboi.agentsim.game.entities.Movement;

/**
 * A timed task that is execute without movement.
 * This class returns none for the movement and ignores events by default.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class MotionlessTimedTask extends TimedTask {
	/**
	 * Creates a new object.
	 * @param duration The duration.
	 */
	public MotionlessTimedTask(final int duration) {
		super(duration);
	}

	/**
	 * Creates a new object.
	 */
	public MotionlessTimedTask() {
	}

	@Override
	public Movement getMovement() {
		return Movement.NONE;
	}

	@Override
	public void eventFailure() {
		// Ignore
	}

	@Override
	public void eventInformationUpdated() {
		// Ignore
	}

}
