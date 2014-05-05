package de.isibboi.agentsim.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.ai.AI;
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

	private final AI _ai;
	private Task _task;

	/**
	 * Creates a new goblin.
	 * 
	 * @param map The map the goblin should interact with.
	 */
	public Goblin(final GameMap map) {
		this(map, map.getRandomValidLocation(Integer.MAX_VALUE));
	}

	/**
	 * Creates a new goblin at the specified location.
	 * 
	 * @param map The map the goblin should interact with.
	 * @param location The location.
	 */
	public Goblin(final GameMap map, final Point location) {
		super(map, location);

		_ai = new GoblinSwarmAI(map);
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
		if (_task == null) {
			calculateNewLocation(random);
		} else {
			_task.update(random);

			if (_task.isFinished()) {
				_task.complete(getMap());
				_task = null;
				_ai.eventTaskFinished();
			}
		}

		handleEntityCollisions(random);

		// This has to be at the end of update, since the AI generates new tasks during the update.
		if (_task == null) {
			_task = _ai.getNewTask();

			if (_task != null) {
				_ai.eventTaskAccepted();
			}
		}
	}

	/**
	 * Checks for colliding entities and calls the AI collision event if a collision happened.
	 * @param random The pseudo random number generator used for randomness.
	 */
	private void handleEntityCollisions(final Random random) {
		for (Entity entity : getMap().getEntitesAt(getLocation(), this)) {
			if (entity instanceof Goblin) {
				_ai.eventCollideWithEntity((Goblin) entity);
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
}