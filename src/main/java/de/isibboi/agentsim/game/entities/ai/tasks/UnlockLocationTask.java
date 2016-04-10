package de.isibboi.agentsim.game.entities.ai.tasks;

import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.drawers.TaskDrawingVisitor;

/**
 * A task that unlocks a location on the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class UnlockLocationTask extends ZeroTimeTask {
	private final GameMap _map;
	private final Point _location;
	private final Entity _entity;

	/**
	 * Creates a task that unlocks the location locked by the given {@code LockLocationTask}.
	 * @param lockLocationTask The {@code LockLocationTask}
	 */
	public UnlockLocationTask(final LockLocationTask lockLocationTask) {
		_map = lockLocationTask.getMap();
		_location = lockLocationTask.getLocation();
		_entity = lockLocationTask.getEntity();
	}

	@Override
	public void zeroTimeAction() {
		_map.unlockLocation(_location, _entity);
		setSuccessful(true);
	}

	@Override
	public void getVisited(final TaskDrawingVisitor visitor) {
		visitor.visit(this);
	}
}