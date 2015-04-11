package de.isibboi.agentsim.game;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * Initializes the game. Generates the initial map and entities and places the entities on the map.
 * @author Sebastian Schmidt
 * @since 0.4.0
 */
public interface GameInitializer {
	/**
	 * Initializes the game. Creates new objects that can be requested using the get methods.
	 * This method should always be called before starting a new game, to ensure that no old and already used objects are returned.
	 * 
	 * @param settings Tells the initializer details about how to initialize the game.
	 */
	void initialize(Settings settings);

	/**
	 * Returns the initial game map.
	 * @return The initial game map.
	 */
	GameMap getMap();

	/**
	 * Returns the initial entities.
	 * @return The initial entities.
	 */
	Entities getEntities();

	/**
	 * Returns the initial entity locations.
	 * @return The initial entity locations.
	 */
	EntityLocationManager getEntityLocationManager();
}
