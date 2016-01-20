package de.isibboi.agentsim.game.entities.ai.intends;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;

/**
 * A list of tasks.
 * Cannot be executed, should only be used for planning.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class CompositeTask implements Task {
	private final List<Task> _tasks = new LinkedList<>();
	private int _duration = 0;

	/**
	 * Adds a task to the end of this composite task.
	 * @param task The task.
	 */
	public void add(final Task task) {
		_tasks.add(task);
		_duration += task.guessDuration();
	}

	@Override
	public int guessDuration() {
		return _duration;
	}

	/**
	 * Returns the tasks.
	 * @return The tasks.
	 */
	public Iterable<? extends Task> getTasks() {
		return Collections.unmodifiableList(_tasks);
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFinished() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean wasSuccessful() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Movement getMovement() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void eventFailure() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void eventInformationUpdated() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void zeroTimeAction() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getProgress() {
		throw new UnsupportedOperationException();
	}
}