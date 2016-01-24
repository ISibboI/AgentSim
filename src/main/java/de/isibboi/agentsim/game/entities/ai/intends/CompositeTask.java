package de.isibboi.agentsim.game.entities.ai.intends;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import de.isibboi.agentsim.game.entities.ai.tasks.Task;
import de.isibboi.agentsim.game.map.Point;

/**
 * Represents a task execution sequence to solve an intend.
 * Create objects using the {@link CompositeTask.Builder} inner class.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public final class CompositeTask {
	/**
	 * A builder that constructs a {@code CompositeTask}.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	public static class Builder {
		private final List<Task> _tasks = new LinkedList<>();
		private int _duration = 0;
		private Point _finishingPoint;

		/**
		 * Creates a new builder for creating a {@link CompositeTask}.
		 */
		public Builder() {
		}

		/**
		 * Adds a task to the end of the composite task.
		 * @param task The task.
		 */
		public void add(final Task task) {
			_tasks.add(task);
			_duration += task.guessDuration();
		}

		/**
		 * Sets the finishing point of the composite task.
		 * That is the point the goblin will be at when the task is finished.
		 * @param finishingPoint The finishing point.
		 */
		public void setFinishingPoint(final Point finishingPoint) {
			_finishingPoint = finishingPoint;
		}

		/**
		 * Creates a composite task from this builder.
		 * @return A new composite task.
		 */
		public CompositeTask build() {
			Objects.requireNonNull(_finishingPoint, "Finishing point was not set!");

			return new CompositeTask(_tasks, _duration, _finishingPoint);
		}
	}

	private final List<Task> _tasks;
	private final int _duration;

	private final Point _finishingPoint;

	/**
	 * Creates a new composite task.
	 * @param tasks The task list.
	 * @param duration The duration of all tasks.
	 * @param finishingPoint The finishing point of the task.
	 */
	private CompositeTask(final List<Task> tasks, final int duration, final Point finishingPoint) {
		_tasks = tasks;
		_duration = duration;
		_finishingPoint = finishingPoint;
	}

	/**
	 * Returns the tasks.
	 * @return The tasks.
	 */
	public Iterable<? extends Task> getTasks() {
		return Collections.unmodifiableList(_tasks);
	}

	/**
	 * Returns the duration of this execution sequence.
	 * @return The duration.
	 */
	public int getDuration() {
		return _duration;
	}

	/**
	 * Returns the point the goblin will be at after the task finished.
	 * @return The finishing point.
	 */
	public Point getFinishingPoint() {
		return _finishingPoint;
	}
}