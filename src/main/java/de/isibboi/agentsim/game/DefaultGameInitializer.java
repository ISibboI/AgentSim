package de.isibboi.agentsim.game;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.entities.GoblinSpawner;
import de.isibboi.agentsim.game.entities.ai.tasks.GoblinTaskFactory;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.MapGenerator;

/**
 * Creates the default game. This is for testing purposes. The word 'default' is documented by source code.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class DefaultGameInitializer implements GameInitializer {
	private GameMap _map;
	private Entities _entities;
	private EntityLocationManager _entityLocationManager;

	@Override
	public void initialize(final Settings settings) {
		MapGenerator mapGenerator = new MapGenerator(settings);
		_map = mapGenerator.generateMap();
		_entities = new Entities();
		_entityLocationManager = new EntityLocationManager(_map, _entities, settings);
		_entityLocationManager.setGoblinSpawner(new GoblinSpawner(_entityLocationManager, new GoblinTaskFactory(), settings));

		_entityLocationManager.getGoblinSpawner().spawnGoblins(settings.getInt(Settings.GAME_INITIAL_GOBLIN_COUNT));
	}

	@Override
	public GameMap getMap() {
		return _map;
	}

	@Override
	public Entities getEntities() {
		return _entities;
	}

	@Override
	public EntityLocationManager getEntityLocationManager() {
		return _entityLocationManager;
	}
}