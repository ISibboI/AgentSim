package de.isibboi.agentsim.game;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * Represents a game of AgentSim.
 * @author Sebastian Schmidt
 * @since 0.4.0
 */
public class Game implements Updateable {
	private final Logger _log = LogManager.getLogger(getClass());

	private final GameInitializer _gameInitializer;
	private final Settings _settings;

	private GameMap _map;
	private Entities _entities;
	private EntityLocationManager _entityLocations;

	private volatile boolean _paused;

	/**
	 * Creates a new game.
	 * @param gameInitializer The Initializer used to start the game.
	 * @param settings The settings.
	 */
	public Game(final GameInitializer gameInitializer, final Settings settings) {
		_gameInitializer = gameInitializer;
		_settings = settings;
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		if (_paused) {
			return;
		}

		_entities.update(random);
	}

	/**
	 * Returns the game map.
	 * @return The game map.
	 */
	public GameMap getMap() {
		return _map;
	}

	/**
	 * Returns the game entities.
	 * @return The game entities.
	 */
	public Entities getEntities() {
		return _entities;
	}

	/**
	 * Starts the game.
	 */
	public void start() {
		_gameInitializer.initialize(_settings);

		_map = _gameInitializer.getMap();
		_entities = _gameInitializer.getEntities();
		_entityLocations = _gameInitializer.getEntityLocationManager();

		_log.info("Game started");
	}

	/**
	 * Restarts the game.
	 */
	public void restart() {
		start();
	}

	/**
	 * Returns true if the game is currently paused.
	 * @return True if the game is currently paused.
	 */
	public boolean isPaused() {
		return _paused;
	}

	/**
	 * Pauses or resumes the game.
	 * @param paused If the game should be paused or resumed.
	 */
	public void setPaused(final boolean paused) {
		if (paused == _paused) {
			_log.warn("Game is already " + (paused ? "paused" : "unpaused"));
			return;
		}

		if (paused) {
			_log.info("Game paused");
		} else {
			_log.info("Game unpaused");
		}

		this._paused = paused;
	}
}
