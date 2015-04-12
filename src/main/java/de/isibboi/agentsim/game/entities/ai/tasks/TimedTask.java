package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

/**
 * Handles task duration.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0 
 */
public abstract class TimedTask implements Task {
	private int _duration;
	private int _timeLeft;

	/**
	 * Creates a new abstract task with the given duration.
	 * @param duration The duration of the task.
	 */
	public TimedTask(final int duration) {
		_duration = duration;
		_timeLeft = duration;
	}

	/**
	 * Returns the time that is needed to complete the task.
	 * @return The remaining time.
	 */
	protected int getTimeLeft() {
		return _timeLeft;
	}

	/**
	 * Returns the progress of the task as value between 0 and 1. 0 means no work has been done, 1 means the task is completed.
	 * @return The progress of the task.
	 */
	protected double getProgress() {
		return 1 - ((double) _timeLeft) / _duration;
	}

	@Override
	public boolean isFinished() {
		return _timeLeft <= 0;
	}

	@Override
	public void update(final Random random) {
		if (_timeLeft > 0) {
			_timeLeft--;
		}
	}
}
