package de.isibboi.agentsim.game.map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.game.entities.Drawable;

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
		return isValidEntityLocation(location.x, location.y);
	}

	/**
	 * Searches randomly for a valid location.
	 * 
	 * @param maxTries The amount of random location that should be checked.
	 * @return A point that represents a valid location on the map, or null, if no location was found after {@code maxTries} tries.
	 */
	public Point getRandomValidLocation(final int maxTries) {
		Point result = new Point();

		for (int i = 0; i < maxTries; i++) {
			result.x = _random.nextInt();
			result.y = _random.nextInt();

			if (isValidEntityLocation(result)) {
				return result;
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
		Point result = new Point();

		for (int i = 0; i < maxTries; i++) {
			result.x = -distance + _random.nextInt(doubleDistance);
			result.y = -distance + _random.nextInt(doubleDistance);

			if (result.x * result.x + result.y * result.y < squaredDistance) {
				result.x += _spawnPoint.x;
				result.y += _spawnPoint.y;

				if (isValidEntityLocation(result)) {
					return result;
				}
			}
		}

		return null;
	}
}