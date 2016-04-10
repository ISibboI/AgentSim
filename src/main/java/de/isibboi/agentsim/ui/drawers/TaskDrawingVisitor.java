package de.isibboi.agentsim.ui.drawers;

import de.isibboi.agentsim.game.entities.ai.tasks.Task;

/**
 * A visitor that draws tasks.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class TaskDrawingVisitor implements Visitor<Task> {
	@Override
	public void visit(final Visitable<Task> object) {
		// Do nothing.
	}
}