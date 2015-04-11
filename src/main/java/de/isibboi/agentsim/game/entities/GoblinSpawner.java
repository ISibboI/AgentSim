package de.isibboi.agentsim.game.entities;

import java.util.Random;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.EntityLocationManager;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * Contains logic to spawn goblins on the map.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class GoblinSpawner {
	private final GameMap _map;
	private final Entities _entities;
	private final EntityLocationManager _entityLocationManager;
	private final Settings _settings;

	private final Random _random = new Random();

	/**
	 * Creates a new goblin spawner.
	 * @param map The map to look for good locations.
	 * @param entities The entities to add the goblins to.
	 * @param entityLocationManager The entity location manager to add the goblins to.
	 * @param settings The settings.
	 */
	public GoblinSpawner(final GameMap map, final Entities entities, final EntityLocationManager entityLocationManager, final Settings settings) {
		_map = map;
		_entities = entities;
		_entityLocationManager = entityLocationManager;
		_settings = settings;
	}

	/**
	 * Spawns a goblin at the given location.
	 * @param location The location
	 */
	public void spawnGoblin(final Point location) {
		Goblin goblin = new Goblin(_map, _entityLocationManager);
		_entities.add(goblin);
		_entityLocationManager.setLocation(goblin, location);
	}

	/**
	* Spawns a goblin at a random location near spawn.
	*/
	public void spawnGoblin() {
		spawnGoblin(getRandomValidLocationNearSpawnPoint(Integer.MAX_VALUE, _settings.getInt(Settings.GAME_SPAWN_RADIUS)));
	}

	/**
	 * Spawns {@code amount} goblins at a random location near spawn.
	 * @param amount The amount of goblins that should be spawned.
	 */
	public void spawnGoblins(final int amount) {
		for (int i = 0; i < amount; i++) {
			spawnGoblin();
		}
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

			if (_map.isValidEntityLocation(result.getX(), result.getY())) {
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
				x += _map.getSpawnPoint().getX();
				y += _map.getSpawnPoint().getY();

				if (_map.isValidEntityLocation(x, y)) {
					return new Point(x, y);
				}
			}
		}

		return null;
	}
}