package de.isibboi.agentsim.game.entities;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.util.Util;

/**
 * An entity that has a location known by the game map.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public abstract class MapEntity implements Entity {
	private final EntityLocationManager _entityLocationManager;
	private Point _oldLocation;
	private Rectangle2D.Double _bounds;
	private boolean _isSelected;

	/**
	 * Creates a new map entity at the given location.
	 * @param entityLocationManager The entity location manager that manages the location of this entity.
	 */
	public MapEntity(final EntityLocationManager entityLocationManager) {
		_entityLocationManager = entityLocationManager;
	}

	/**
	 * Returns the current location of this entity.
	 * @return The location of this entity.
	 */
	public Point getLocation() {
		return _entityLocationManager.getLocation(this);
	}

	/**
	 * Sets the location of this entity.
	 * @param location The new location.
	 */
	public void setLocation(final Point location) {
		_entityLocationManager.setLocation(this, location);
	}

	/**
	 * Returns the game map this entity belongs to.
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _entityLocationManager.getMap();
	}

	/**
	 * Returns the entity location manager.
	 * @return The entity location manager.
	 */
	public EntityLocationManager getEntityLocationManager() {
		return _entityLocationManager;
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		_oldLocation = getLocation();
	}

	/**
	 * Returns the location of the entity from the last update.
	 * @return The old location.
	 */
	protected Point getOldLocation() {
		return _oldLocation;
	}

	@Override
	public void draw(final Graphics2D g, final double transition) {
		if (transition < 0 || transition > 1) {
			throw new IllegalArgumentException("Transition must be in the interval [0, 1]!");
		}

		Point location = getLocation();

		if (_oldLocation == null) {
			_oldLocation = location;
		}

		double x = (1 - transition) * _oldLocation.getX() + transition * location.getX();
		double y = (1 - transition) * _oldLocation.getY() + transition * location.getY();

		AffineTransform old = g.getTransform();
		g.translate(x, y);

		Point2D.Double a = new Point2D.Double(0, 0);
		Point2D.Double b = new Point2D.Double(1, 1);
		g.getTransform().transform(a, a);
		g.getTransform().transform(b, b);
		_bounds = Util.createRectangle(a, b);

		draw(g);

		g.setTransform(old);
	}

	/**
	 * Returns the current bounds of this entity as seen on the map.
	 * 
	 * @return The current bounds of this entity.
	 */
	public Rectangle2D.Double getBounds() {
		return _bounds;
	}

	/**
	 * Draws the entity in the unit rectangle, with coordinates from (0, 0) to (1, 1).
	 * @param g The graphics object used for drawing.
	 */
	protected abstract void draw(final Graphics2D g);

	@Override
	public void setSelected(final boolean isSelected) {
		_isSelected = isSelected;
	}

	@Override
	public boolean isSelected() {
		return _isSelected;
	}
}