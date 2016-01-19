package de.isibboi.agentsim.game.entities.ai.tasks;

/**
 * Handles priorities.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class AbstractTask implements Task {
	private int _priority = -1;

	@Override
	public int getPriority() {
		if (_priority < 0) {
			throw new IllegalStateException("Priority was not set.");
		}

		return _priority;
	}

	@Override
	public void setPriority(final int priority) {
		if (priority < 0) {
			throw new IllegalArgumentException("Priority must not be negative.");
		}

		_priority = priority;
	}

	@Override
	public void zeroTimeAction() {
		// Ignore.
	}
}