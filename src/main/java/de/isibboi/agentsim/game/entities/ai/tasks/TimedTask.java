package de.isibboi.agentsim.game.entities.ai.tasks;

import java.util.Random;

/**
 * Handles task duration.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0 
 */
public abstract class TimedTask extends AbstractTask {

	private int _duration;
	private int _timeLeft;
	private boolean _eventFinishedFired = false;
	private boolean _wasSuccessful = true;
	private boolean _isStarted = false;

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

	@Override
	public double getProgress() {
		return 1 - ((double) _timeLeft) / _duration;
	}

	@Override
	public boolean isFinished() {
		return _timeLeft <= 0;
	}

	@Override
	public void update(final Random random, final int tick) {
		if (!_isStarted) {
			_isStarted = true;
			eventStarted();
		}

		if (_timeLeft > 0) {
			_timeLeft--;
		}

		if (_timeLeft <= 0 && !_eventFinishedFired) {
			eventFinished(tick);
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
	 * @param tick The current tick.
	 */
	protected abstract void eventFinished(int tick);

	/**
	 * Called when the task is started.
	 */
	protected abstract void eventStarted();

	/**
	 * Sets the duration.
	 * @param duration The duration.
	 */
	protected void setDuration(final int duration) {
		_duration = duration;
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
