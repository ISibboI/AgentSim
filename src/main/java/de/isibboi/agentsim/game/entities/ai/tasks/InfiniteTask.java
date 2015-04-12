package de.isibboi.agentsim.game.entities.ai.tasks;

/**
 * An infinite task that forbids calls to {@link #isFinished()} and {@link #guessDuration()}. Also, the events are ignored.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class InfiniteTask implements Task {
	@Override
	public boolean isFinished() {
		throw new UnsupportedOperationException("Operation not supported!");
	}

	@Override
	public int guessDuration() {
		throw new UnsupportedOperationException("Operation not supported!");
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
