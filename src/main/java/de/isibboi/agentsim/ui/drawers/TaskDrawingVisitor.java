package de.isibboi.agentsim.ui.drawers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.entities.MapEntity;
import de.isibboi.agentsim.game.entities.Movement;
import de.isibboi.agentsim.game.entities.ai.tasks.MoveToTask;
import de.isibboi.agentsim.game.entities.ai.tasks.Task;
import de.isibboi.agentsim.game.map.Point;

/**
 * A visitor that draws tasks.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class TaskDrawingVisitor implements Visitor<Task> {
	private static final Logger LOG = LogManager.getLogger(TaskDrawingVisitor.class);

	private Graphics2D _g;
	private double _transition;

	@Override
	public void visit(final Visitable<Task, ? extends Visitor<Task>> object) {
		LOG.trace("Doing nothing for " + object);
	}

	/**
	 * Draws the planned path of a {@link MoveToTask}.
	 * @param moveToTask The task to draw.
	 */
	public void visit(final MoveToTask moveToTask) {
		LOG.trace("Drawing the path of " + moveToTask);

		Queue<Movement> movementQueue = moveToTask.getMovementQueue();
		MapEntity entity = moveToTask.getEntity();

		Point current = entity.getLocation();
		Color color = new Color(200, 250, 50, 127);

		for (Movement movement : movementQueue) {
			current = movement.move(current);
			_g.setColor(color);
			_g.fillRect(current.getX(), current.getY(), 1, 1);
		}
	}

	/**
	 * Sets the graphics object for drawing.
	 * @param g The graphics object for drawing.
	 */
	public void setGraphics(final Graphics2D g) {
		_g = g;
	}

	/**
	 * Sets the transition value for drawing.
	 * @param transition The transition value for drawing.
	 */
	public void setTransition(final double transition) {
		_transition = transition;
	}
}