package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles task duration.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0 
 */
public abstract class TimedTask implements Task {
	private final Logger _log = LogManager.getLogger(getClass());

	private int _duration;
	private int _timeLeft;
	private boolean _eventFinishedFired;
	private boolean _wasSuccessful = true;

	/**
	 * Creates a new timed task.
	 */
	public TimedTask() {
	}

	/**
	 * Creates a new timed task with the given duration.
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
	public void update(final Random random, final int tick) {
		if (_timeLeft > 0) {
			_timeLeft--;
		}

		if (_timeLeft <= 0 && !_eventFinishedFired) {
			eventFinished();
			_eventFinishedFired = true;
		}
	}

	/**
	 * Returns the duration of this task.
	 * @return The duration of this task.
	 */
	public int getDuration() {
		return _duration;
	}

	/**
	 * Called when the task is finished.
	 */
	protected abstract void eventFinished();

	/**
	 * Sets the duration.
	 * @param duration The duration.
	 * @param duration
	 */
	protected void setDuration(final int duration) {
		_duration = duration;
	}

	@Override
	public void start() {
		_timeLeft = _duration;
		_eventFinishedFired = false;
	}

	/**
	 * Aborts the task.
	 * Does not fire {@link #eventFinished()}.
	 */
	protected void fail() {
		_eventFinishedFired = true;
		_timeLeft = 0;
		_wasSuccessful = false;
	}

	@Override
	public boolean wasSuccessful() {
		return _wasSuccessful;
	}
}
