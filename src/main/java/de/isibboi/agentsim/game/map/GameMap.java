package de.isibboi.agentsim.game.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.Entity;

/**
 * Represents the game map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class GameMap implements Drawable {
	private final BufferedImage _map;

	private final Point _spawnPoint;
	private final MaterialFactory _materialFactory = Environment.MATERIAL_FACTORY;
	private final Random _random = new Random();

	private final Multimap<Point, Entity> _entityLocations = HashMultimap.create();

	/**
	 * Creates the map from the given image.
	 * 
	 * @param image The map image.
	 * @param spawnPoint The spawn point.
	 */
	public GameMap(final BufferedImage image, final Point spawnPoint) {
		_map = image;
		_spawnPoint = spawnPoint;
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
	 * Searches randomly for a valid location.
	 * 
	 * @param maxTries The amount of random location that should be checked.
	 * @return A point that represents a valid location on the map, or null, if no location was found after {@code maxTries} tries.
	 */
	public Point getRandomValidLocation(final int maxTries) {
		Point.Builder result = new Point.Builder();

		for (int i = 0; i < maxTries; i++) {
			result.setXY(_random.nextInt(), _random.nextInt());

			if (isValidEntityLocation(result.getX(), result.getY())) {
				return result.build();
			}
		}

		return null;
	}

	/**
	 * Searches randomly for a valid location near spawn.
	 * 
	 * @param maxTries The amount of random location that should be checked.
	 * @param distance The maximum distance from spawn.
	 * @return A point that represents a valid location on the map, or null, if no location was found after {@code maxTries} tries.
	 */
	public Point getRandomValidLocationNearSpawnPoint(final int maxTries, final int distance) {
		int squaredDistance = distance * distance;
		int doubleDistance = distance << 1;
		int x;
		int y;

		for (int i = 0; i < maxTries; i++) {
			x = -distance + _random.nextInt(doubleDistance);
			y = -distance + _random.nextInt(doubleDistance);

			if (x * x + y * y < squaredDistance) {
				x += _spawnPoint.getX();
				y += _spawnPoint.getY();

				if (isValidEntityLocation(x, y)) {
					return new Point(x, y);
				}
			}
		}

		return null;
	}

	/**
	 * Sets the location of the given entity. If oldLocation is != null, the old location will be deleted before inserting the new one.
	 * 
	 * @param entity The entity.
	 * @param oldLocation The old location of the entity.
	 * @param newLocation The new location of the entity.
	 */
	public void updateLocation(final Entity entity, final Point oldLocation, final Point newLocation) {
		if (oldLocation != null) {
			if (!_entityLocations.get(oldLocation).remove(entity)) {
				throw new IllegalArgumentException("Given entity " + entity + " was not at location " + oldLocation);
			}
		}

		_entityLocations.put(newLocation, entity);
	}

	/**
	 * Returns all entities at the given location.
	 * @param location The location.
	 * @return A collection containing all entities at the given point.
	 */
	public Collection<Entity> getEntitiesAt(final Point location) {
		return _entityLocations.get(location);
	}

	/**
	 * Returns all entities at the given location, except self.
	 * @param location The location.
	 * @param self The entity that should be excluded.
	 * @return A collection containing all entities at the given point, except self.
	 */
	public Collection<Entity> getEntitesAt(final Point location, final Entity self) {
		Collection<Entity> result = new ArrayList<>();
		result.addAll(_entityLocations.get(location));
		result.remove(self);
		return result;
	}
}