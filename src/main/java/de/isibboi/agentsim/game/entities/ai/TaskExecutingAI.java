package de.isibboi.agentsim.game.entities.ai;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Attributes;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.entities.ai.intends.CompositeTask;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;

/**
 * An AI with a task queue. If the first task is finished, it is disposed and the next task is executed.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class TaskExecutingAI implements AI {
	private static final Logger LOG = LogManager.getLogger(TaskExecutingAI.class);

	private final Queue<Task> _taskQueue = new LinkedList<>();
	private Task _currentTask;

	/**
	 * The task that is executed when there is no task to execute.
	 */
	private Task _idleTask;
	private boolean _firedExecutionFinished;

	/**
	 * Creates a new object.
	 */
	public TaskExecutingAI() {
		_firedExecutionFinished = false;
	}

	/**
	 * Updates the AI.
	 * Executes tasks one by one.
	 * If a task finishes during one update cycle, the next is started.
	 * At most one task is updated during one cycle.
	 * If a task is finished immediately, it will not use up an update cycle.
	 * 
	 * @param attributes The current attributes of the controlled entity.
	 * @param random The pseudo random number generator used for randomness.
	 * @param tick The current tick.
	 * @throws GameUpdateException If updating the AI goes wrong. 
	 */
	@Override
	public void update(final Attributes attributes, final Random random, final int tick) throws GameUpdateException {
		if (_currentTask != null) {
			if (_currentTask.isFinished()) {
				finishTask(attributes, random, tick);
			} else {
				_currentTask.update(random, tick);
			}
		} else {
			startNextTask(_taskQueue.poll(), attributes, random, tick);
		}
	}

	/**
	 * Called when a task is finished.
	 * Fires the correct events.
	 * 
	 * @param attributes The current attributes of the controlled entity.
	 * @param random The pseudo random number generator used for randomness.
	 * @param tick The current tick.
	 * @throws GameUpdateException If finishing the task goes wrong. 
	 */
	private void finishTask(final Attributes attributes, final Random random, final int tick) throws GameUpdateException {
		Task lastTask = _currentTask;
		_currentTask = null;

		if (lastTask.wasSuccessful()) {
			if (_taskQueue.isEmpty()) {
				eventTaskFinished(lastTask, 1);
				update(attributes, random, tick);
			} else {
				Task nextTask = _taskQueue.poll();
				eventTaskFinished(lastTask, nextTask.guessDuration());
				startNextTask(nextTask, attributes, random, tick);
			}
		} else {
			LOG.trace("Task was not executed successfully: " + lastTask);

			eventExecutionAborted();
			update(attributes, random, tick);
		}
	}

	/**
	 * Starts the given task.
	 * Must not be called if {@link #_currentTask} is not {@code null}.
	 * 
	 * @param task The task to start.
	 * @param attributes The current attributes of the controlled entity.
	 * @param random The pseudo random number generator used for randomness.
	 * @param tick The current tick.
	 * @throws GameUpdateException If finishing the task goes wrong. 
	 */
	private void startNextTask(final Task task, final Attributes attributes, final Random random, final int tick) throws GameUpdateException {
		if (_currentTask != null) {
			throw new IllegalStateException("Cannot start next task before current one is finished.");
		}

		_currentTask = task;

		if (_currentTask != null) {
			_firedExecutionFinished = false;
			_currentTask.zeroTimeAction();
			update(attributes, random, tick);
		} else {
			if (!_firedExecutionFinished) {
				eventExecutionFinished();
				_firedExecutionFinished = true;
			}

			eventExecutingIdleTask();

			// If a task was added due to events, it should be started instead of updating the idle task.
			if (_taskQueue.isEmpty()) {
				_idleTask.update(random, tick);
			} else {
				update(attributes, random, tick);
			}
		}
	}

	@Override
	public Movement getMovement() {
		if (_currentTask == null && _idleTask != null) {
			return _idleTask.getMovement();
		}

		if (_currentTask != null) {
			return _currentTask.getMovement();
		} else {
			return null;
		}
	}

	/**
	 * Enqueues the given task.
	 * @param task The task.
	 */
	public void enqueueTask(final Task task) {
		Objects.requireNonNull(task);

		if (task instanceof CompositeTask) {
			enqueueTasks(((CompositeTask) task).getTasks());
		} else {
			_taskQueue.add(task);
		}
	}

	/**
	 * Enqueues the given tasks.
	 * @param tasks The tasks.
	 */
	public void enqueueTasks(final Iterable<? extends Task> tasks) {
		for (Task task : tasks) {
			enqueueTask(task);
		}
	}

	/**
	 * Returns false if the queue is empty and no task is executed, except the idle task.
	 * @return False if the queue is empty and no task is executed, except the idle task.
	 */
	public boolean isExecuting() {
		return _currentTask != null;
	}

	/**
	 * Sets the task that should be executed when idling.
	 * @param task The idle task.
	 */
	public void setIdleTask(final Task task) {
		_idleTask = task;
	}

	/**
	 * Fired when the execution of a task has been finished.
	 * @param task The task that was finished.
	 * @param nextTaskDuration The guesstimated duration of the next task.
	 */
	protected abstract void eventTaskFinished(Task task, int nextTaskDuration);

	/**
	 * Fired when the execution is aborted due to a failed task.
	 */
	protected abstract void eventExecutionAborted();

	/**
	 * Fired when all tasks have been executed.
	 */
	protected abstract void eventExecutionFinished();

	/**
	 * Fired when the idle task would be executing.
	 * To prevent this, add another task to the queue before this method returns.
	 * It is fired once per update, until another task is executing.
	 */
	protected abstract void eventExecutingIdleTask();

	/**
	 * Returns a guess of the time that executing all tasks, enclosing the currently executing task, takes.
	 * @return The guessed time to finish all tasks.
	 */
	public int guessDurationToFinishQueue() {
		int duration = 0;

		for (Task task : _taskQueue) {
			duration += task.guessDuration();
		}

		if (_currentTask != null) {
			duration += _currentTask.guessDuration();
		}

		return duration;
	}

	/**
	 * Aborts the execution of all tasks.
	 */
	public void abort() {
		_taskQueue.clear();

		if (_currentTask != null) {
			_currentTask.eventFailure();
			_currentTask = null;
		}
	}
}