package de.isibboi.agentsim.game.map;

import java.awt.image.BufferedImage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.algorithm.LockManager;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.ai.KnowledgeProvider;
import de.isibboi.agentsim.ui.renderer.Renderer;

/**
 * Represents the game map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class GameMap implements Drawable, KnowledgeProvider<Material> {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(GameMap.class);

	private final BufferedImage _map;
	private final MaterialFactory _materialFactory = Environment.MATERIAL_FACTORY;

	private final Point _spawnPoint;
	private final LockManager<Point, Entity> _lockManager = new LockManager<>();

	/**
	 * The age of the map. That is the total amount of modifications made to the map.
	 */
	private long _age = 1; // Needs to be one for knowledge maps to work.

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
	 * Locks the given location if possible.
	 * @param location The location.
	 * @param entity The entity that tries to lock the location.
	 * @return True if the location was locked successfully, false if it was already locked.
	 */
	public boolean tryLockLocation(final Point location, final Entity entity) {
		return _lockManager.tryLock(location, entity);
	}

	/**
	 * Checks if the given location is locked.
	 * @param location The location.
	 * @return True if the location is locked, false otherwise.
	 */
	public boolean isLocationLocked(final Point location) {
		return _lockManager.isLocked(location);
	}

	/**
	 * Unlocks the given location.
	 * @param location The location.
	 * @param entity The entity that unlocks the location.
	 */
	public void unlockLocation(final Point location, final Entity entity) {
		_lockManager.unlock(location, entity);
	}

	/**
	 * Unlocks all locations that are currently locked by the given entity.
	 * @param entity The entity.
	 */
	public void unlockAllLocations(final Entity entity) {
		_lockManager.unlockAll(entity);
	}

	/**
	 * Sets the given material at the given location.
	 * 
	 * @param location The location.
	 * @param material The material.
	 */
	public void setMaterial(final Point location, final Material material) {
		_map.setRGB(location.getX(), location.getY(), material.getColor());
		_age++;
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
	 * Returns the spawn point.
	 * @return The spawn point.
	 */
	public Point getSpawnPoint() {
		return _spawnPoint;
	}

	/**
	 * Returns the age of the map.
	 * @return The age of the map.
	 */
	public long getAge() {
		return _age;
	}

	/**
	 * Returns the width of the map.
	 * @return The width of the map.
	 */
	public int getWidth() {
		return _map.getWidth();
	}

	/**
	 * Returns the height of the map.
	 * @return The height of the map.
	 */
	public int getHeight() {
		return _map.getHeight();
	}

	@Override
	public Material getKnowledge(final Point location) {
		return getMaterialAt(location);
	}

	@Override
	public void accept(final Renderer renderer) {
		renderer.visit(this);
	}
}
