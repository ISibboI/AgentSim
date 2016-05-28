package de.isibboi.agentsim.game.entities;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.renderer.Renderer;

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

	/**
	 * Returns the current bounds of this entity as seen on the map.
	 * 
	 * @return The current bounds of this entity.
	 */
	public Rectangle2D.Double getBounds() {
		return _bounds;
	}

	@Override
	public void setSelected(final boolean isSelected) {
		_isSelected = isSelected;
	}

	@Override
	public boolean isSelected() {
		return _isSelected;
	}

	@Override
	public void accept(final Renderer renderer) {
		renderer.visit(this);
	}
}