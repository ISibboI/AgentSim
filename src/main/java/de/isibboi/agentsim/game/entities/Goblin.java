package de.isibboi.agentsim.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.ai.AI;
import de.isibboi.agentsim.game.entities.ai.GoblinSwarmAI;
import de.isibboi.agentsim.game.entities.ai.Task;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * A goblin that digs dirt.
 * @author Sebastian Schmidt
 * @since 0.1.0
 */
public class Goblin extends MapEntity {
	private final Color _color = new Color(0x55bb55);

	private GoblinAttributes _attributes;
	private final AI _ai;
	private Task _task;

	/**
	 * Creates a new goblin at the specified location.
	 * 
	 * @param map The map the goblin should interact with.
	 * @param entityLocationManager The entity location manager that manages the location of this goblin.
	 */
	public Goblin(final GameMap map, final EntityLocationManager entityLocationManager) {
		super(map, entityLocationManager);

		_ai = new GoblinSwarmAI(map, entityLocationManager, this);
	}

	/**
	 * Returns the AI of this goblin.
	 * 
	 * @return The AI of this goblin.
	 */
	public AI getAI() {
		return _ai;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(_color);
		g.fillRect(getLocation().getX(), getLocation().getY(), 1, 1);
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		if (!_ai.update(random)) {
			return;
		}

		if (_task == null) {
			calculateNewLocation(random);
		} else {
			_task.update(random);

			if (_task.isFinished()) {
				_task.complete(getMap(), getEntityLocationManager());
				_task = null;
				_ai.eventTaskFinished();
			}
		}

		// This has to be at the end of update, since the AI generates new tasks during the update.
		if (_task == null) {
			_task = _ai.getNewTask();

			if (_task != null) {
				_ai.eventTaskAccepted();
			}
		}
	}

	/**
	 * Calculates the new location of the goblin.
	 * @param random The pseudo random number generator used for randomness.
	 * @throws GameUpdateException If the number generator generates a wrong direction.
	 */
	private void calculateNewLocation(final Random random) throws GameUpdateException {
		int x = getLocation().getX();
		int y = getLocation().getY();

		switch (random.nextInt(4)) {
		case 0:
			x++;
			break;
		case 1:
			x--;
			break;
		case 2:
			y++;
			break;
		case 3:
			y--;
			break;
		default:
			throw new GameUpdateException("Illegal direction");
		}

		Point newLocation = new Point(x, y);

		if (getMap().isValidEntityLocation(newLocation)) {
			setLocation(newLocation);
			_ai.eventMoveTo(newLocation);
		} else if (getMap().isLocationOnMap(newLocation)) {
			_ai.eventCollideWithWall(newLocation);
		} else {
			_ai.eventCollideWithMapBorder(newLocation);
		}
	}

	@Override
	public void collideWith(final Entity entity) {
		_ai.eventCollideWithEntity(entity);
	}
}