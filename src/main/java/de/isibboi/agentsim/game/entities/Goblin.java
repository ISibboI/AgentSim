package de.isibboi.agentsim.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

import de.isibboi.agentsim.game.map.GameMap;

/**
 * A goblin that digs dirt.
 * @author Sebastian Schmidt
 * @since 0.1.0
 */
public class Goblin implements Entity {
	private final GameMap _map;
	private final Point _location;
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
	public void update() {
		// TODO Auto-generated method stub

	}

}