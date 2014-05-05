package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * A task that is bound to a certain location on the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public abstract class AbstractLocalTask extends AbstractTask {
	private final Point _location;
	private final GameMap _map;

	/**
	 * Creates a new abstract local task.
	 * 
	 * @param duration The duration of the task.
	 * @param location The location the task is bound to.
	 * @param map The game map.
	 */
	public AbstractLocalTask(final int duration, final Point location, final GameMap map) {
		super(duration);

		_location = location;
		_map = map;
	}

	/**
	 * Returns the location this task is bound to.
	 * 
	 * @return The location.
	 */
	public Point getLocation() {
		return _location;
	}

	/**
	 * Returns the game map.
	 * 
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _map;
	}
}