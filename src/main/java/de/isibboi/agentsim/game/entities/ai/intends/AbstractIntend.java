package de.isibboi.agentsim.game.entities.ai.intends;

/**
 * Handles priorities and the age of an intend.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class AbstractIntend implements Intend {
	private final int _creationTime;
	private final int _priority;

	/**
	 * Creates a new abstract intend with the given creation tick.
	 * @param tick The current tick.
	 * @param priority The priority of the intend.
	 */
	public AbstractIntend(final int tick, final int priority) {
		_creationTime = tick;
		_priority = priority;
	}

	@Override
	public int getPriority() {
		return _priority;
	}

	@Override
	public int getLastUpdateTime() {
		return _creationTime;
	}
}