package de.isibboi.agentsim.game.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Drawable;

/**
 * Represents the game map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class GameMap implements Drawable {
	private final Logger _log = LogManager.getLogger(getClass());

	private final BufferedImage _map;
	private final MaterialFactory _materialFactory = Environment.MATERIAL_FACTORY;

	private final Settings _settings;

	private final Point _spawnPoint;
	private final Set<Point> _locationLocks = new HashSet<Point>();

	/**
	 * Creates the map from the given image.
	 * 
	 * @param image The map image.
	 * @param spawnPoint The spawn point.
	 * @param settings The settings.
	 */
	public GameMap(final BufferedImage image, final Point spawnPoint, final Settings settings) {
		_map = image;
		_spawnPoint = spawnPoint;
		_settings = settings;
	}

	@Override
	public void draw(final Graphics2D g) {
		g.drawImage(_map, 0, 0, null);
	}

	/**
	 * Return true if an entity is allowed to be at the specified location.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return True if the given location is valid for an entity.
	 */
	public boolean isValidEntityLocation(final int x, final int y) {
		if (x < 0 || y < 0 || x >= _map.getWidth() || y >= _map.getHeight()) {
			return false;
		}

		return !_materialFactory.getMaterial(_map.getRGB(x, y)).isSolid();
	}

	/**
	 * Return true if an entity is allowed to be at the specified location.
	 * @param location The location.
	 * @return True if the given location is valid for an entity.
	 */
	public boolean isValidEntityLocation(final Point location) {
		return isValidEntityLocation(location.getX(), location.getY());
	}

	/**
	 * Returns the material at the given location.
	 * 
	 * @param location The location.
	 * @return The material
	 */
	public Material getMaterialAt(final Point location) {
		return _materialFactory.getMaterial(_map.getRGB(location.getX(), location.getY()));
	}

	/**
	 * Locks the given location.
	 * @param location The location.
	 */
	public void lockLocation(final Point location) {
		_locationLocks.add(location);
	}

	/**
	 * Checks if the given location is locked.
	 * @param location The location.
	 * @return True if the location is locked, false otherwise.
	 */
	public boolean isLocationLocked(final Point location) {
		return _locationLocks.contains(location);
	}

	/**
	 * Unlocks the given location.
	 * @param location The location.
	 */
	public void unlockLocation(final Point location) {
		_locationLocks.remove(location);
	}

	/**
	 * Sets the given material at the given location.
	 * 
	 * @param location The location.
	 * @param material The material.
	 */
	public void setMaterial(final Point location, final Material material) {
		_map.setRGB(location.getX(), location.getY(), material.getColor());
	}

	/**
	 * Returns true if the given location is on the map.
	 * @param location The location.
	 * @return True if {@code location} is on the map.
	 */
	public boolean isLocationOnMap(final Point location) {
		return location.getX() >= 0 && location.getY() >= 0 && location.getX() < _map.getWidth() && location.getY() < _map.getHeight();
	}

	/**
	 * Returns the settings.
	 * @return The settings.
	 */
	public Settings getSettings() {
		return _settings;
	}

	/**
	 * Returns the spawn point.
	 * @return The spawn point.
	 */
	public Point getSpawnPoint() {
		return _spawnPoint;
	}
}
