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
 * @since 0.4.0
 */
public class EntityLocationManager implements Updateable {
	private final GameMap _map;
	private final Entities _entities;
	private final Settings _settings;

	private final GoblinSpawner _goblinSpawner;

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

		_goblinSpawner = new GoblinSpawner(map, entities, this, settings);
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
	 * Sets the location of the given entity.
	 * @param entity The entity.
	 * @param location The location of the entity.
	 */
	public void setLocation(final Entity entity, final Point location) {
		_entityLocations.update(location, entity);
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		checkForEntityCollisions();
	}

	/**
	 * Checks if any entities collide and fires the appopropriate collision events.
	 */
	private void checkForEntityCollisions() {
		Set<Point> locations = _entityLocations.keySet();

		for (Point location : locations) {
			Collection<Entity> tmpEntities = getEntitiesAt(location);
			Entity[] entities = new Entity[tmpEntities.size()];
			entities = tmpEntities.toArray(entities);

			for (int i = 0; i < entities.length; i++) {
				for (int j = i + 1; j < entities.length; j++) {
					entities[i].collideWith(entities[j]);
				}
			}
		}
	}

	/**
	 * Removes the given entity.
	 * @param entity The entity to remove.
	 */
	public void removeEntity(final Entity entity) {
		_entityLocations.removeValue(entity);
		_entities.remove(entity);
	}
}