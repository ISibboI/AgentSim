package de.isibboi.agentsim.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * A goblin that digs dirt.
 * @author Sebastian Schmidt
 * @since 0.1.0
 */
public class Goblin implements Entity {
	private final GameMap _map;
	private Point _location;
	private final Color _color = new Color(0x55bb55);

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
		if (!map.isValidEntityLocation(location)) {
			throw new IllegalArgumentException("Location is not valid: " + location);
		}

		_map = map;
		_location = location;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.setColor(_color);
		g.fillRect(_location.x, _location.y, 1, 1);
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		calculateNewLocation(random);
	}

	/**
	 * Calculates the new location of the goblin.
	 * @param random The pseudo random number generator used for randomness.
	 * @throws GameUpdateException If the number generator generates a wrong direction.
	 */
	private void calculateNewLocation(final Random random) throws GameUpdateException {
		Point newLocation = new Point(_location);

		switch (random.nextInt(4)) {
		case 0:
			newLocation.x++;
			break;
		case 1:
			newLocation.x--;
			break;
		case 2:
			newLocation.y++;
			break;
		case 3:
			newLocation.y--;
			break;
		default:
			throw new GameUpdateException("Illegal direction");
		}
		
		if (_map.isValidEntityLocation(newLocation)) {
			_location = newLocation;
		} 
	}
}