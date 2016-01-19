package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Movement;

/**
 * A task that either completes immediately, or fails immediately.
 * It never uses up an update cycle.
 * Tasks extending this class must implement the {@link #zeroTimeAction()} method.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public abstract class ZeroTimeTask extends AbstractTask {
	private boolean _wasSuccessful = false;
	private boolean _wasSuccessfulSet = false;

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public boolean wasSuccessful() {
		if (!_wasSuccessfulSet) {
			throw new IllegalStateException("_wasSuccessful was not set!");
		}

		return _wasSuccessful;
	}

	@Override
	public Movement getMovement() {
		throw new UnsupportedOperationException("A ZeroTimeTask does not make the entity move!");
	}

	@Override
	public int guessDuration() {
		return 0;
	}

	@Override
	public void eventFailure() {
		// Ignore.
	}

	@Override
	public void eventInformationUpdated() {
		// Ignore.
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		throw new UnsupportedOperationException("A ZeroTimeTask cannot be updated!");
	}

	/**
	 * Sets if the {@code ZeroTimeTask} was successful.
	 * @param successful True if the task was successful, false otherwise.
	 */
	protected void setSuccessful(final boolean successful) {
		_wasSuccessfulSet = true;
		_wasSuccessful = successful;
	}
}
