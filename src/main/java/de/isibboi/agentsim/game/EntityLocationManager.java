package de.isibboi.agentsim.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.algorithm.BidirectionalHashMultimap;
import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.entities.Entity;
import de.isibboi.agentsim.game.entities.GoblinSpawner;
import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;

/**
 * Manages the locations of the entities on the map. This class is intended to execute complex movement orders.
 * It should not do path finding, but move an entity along a path.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class EntityLocationManager implements Updateable {
	private final GameMap _map;
	private final Entities _entities;
	private final Settings _settings;
	private final EntityCollider _entityCollider;

	private GoblinSpawner _goblinSpawner;

	private final BidirectionalHashMultimap<Point, Entity> _entityLocations = new BidirectionalHashMultimap<>();

	/**
	 * Creates a new entity location manager.
	 * @param map The map the entities operate on.
	 * @param entities The entities to manage.
	 * @param settings The settings.
	 */
	public EntityLocationManager(final GameMap map, final Entities entities, final Settings settings) {
		_map = map;
		_entities = entities;
		_settings = settings;

		if (settings.getInt(Settings.CORE_UPDATE_COLLISION_THREAD_COUNT) > 1) {
			_entityCollider = new MultiThreadedEntityCollider(settings);
		} else {
			_entityCollider = new SimpleEntityCollider(settings);
		}
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

	/**
	 * Returns the location of the given entity.
	 * @param e The entity.
	 * @return The location of {@code e}.
	 */
	public Point getLocation(final Entity e) {
		return _entityLocations.getKey(e);
	}

	/**
	 * Returns the goblin spawner.
	 * @return The goblin spawner.
	 */
	public GoblinSpawner getGoblinSpawner() {
		return _goblinSpawner;
	}

	/**
	 * Sets the goblin spawner.
	 * @param goblinSpawner The goblin spawner.
	 */
	public void setGoblinSpawner(final GoblinSpawner goblinSpawner) {
		_goblinSpawner = goblinSpawner;
	}

	/**
	 * Sets the location of the given entity.
	 * @param entity The entity.
	 * @param location The location of the entity.
	 */
	public void setLocation(final Entity entity, final Point location) {
		_entityLocations.update(location, entity);
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		checkForEntityCollisions();
	}

	/**
	 * Checks if any entities collide and fires the appropriate collision events.
	 */
	private void checkForEntityCollisions() {
		_entityCollider.startCollision();
		Set<Point> locations = _entityLocations.keySet();

		for (Point location : locations) {
			Collection<Entity> tmpEntities = getEntitiesAt(location);

			if (tmpEntities.size() > 1) {
				Entity[] entities = new Entity[tmpEntities.size()];
				entities = tmpEntities.toArray(entities);

				for (int i = 0; i < entities.length; i++) {
					for (int j = i + 1; j < entities.length; j++) {
						_entityCollider.collide(entities[i], entities[j]);
					}
				}

				_entityCollider.finishBlock();
			}
		}

		_entityCollider.finishCollision();
	}

	/**
	 * Removes the given entity.
	 * @param entity The entity to remove.
	 */
	public void removeEntity(final Entity entity) {
		_entityLocations.removeValue(entity);
		_entities.remove(entity);
		_map.unlockAllLocations(entity);
	}

	/**
	 * Returns the entities.
	 * @return The entities.
	 */
	public Entities getEntities() {
		return _entities;
	}

	/**
	 * Returns the game map.
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _map;
	}

	/**
	 * Returns the settings.
	 * @return The settings.
	 */
	public Settings getSettings() {
		return _settings;
	}

	/**
	 * Shuts the underlying {@link EntityCollider} down.
	 */
	public void shutdown() {
		_entityCollider.shutdown();
	}
}