package de.isibboi.agentsim.game;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.ui.GameStatusMessageListener;

/**
 * Represents a game of AgentSim.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class Game implements Updateable {
	private final Logger _log = LogManager.getLogger(getClass());

	private final GameInitializer _gameInitializer;
	private final Settings _settings;
	private final GameStatusMessageListener _gameStatusMessageListener;

	private GameMap _map;
	private Entities _entities;
	private EntityLocationManager _entityLocationManager;

	private volatile boolean _paused;

	/**
	 * Creates a new game.
	 * @param gameInitializer The initializer used to start the game.
	 * @param settings The settings.
	 * @param gameStatusMessageListener The listener for game status messages.
	 */
	public Game(final GameInitializer gameInitializer, final Settings settings, final GameStatusMessageListener gameStatusMessageListener) {
		_gameInitializer = gameInitializer;
		_settings = settings;
		_gameStatusMessageListener = gameStatusMessageListener;
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		if (_paused) {
			return;
		}

		_entities.update(random, tick);
		_entityLocationManager.update(random, tick);

		checkGameOver();
	}

	/**
	 * Checks if the game over conditions are met. If yes, a game over status message is sent and the game is paused. 
	 */
	private void checkGameOver() {
		if (_entities.isEmpty()) {
			gameOver();
		}
	}

	/**
	 * Ends the game.
	 */
	private void gameOver() {
		_gameStatusMessageListener.receiveGameOverMessage("Game over :(");
		setPaused(true);

		_log.info("Game over");
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
		_entityLocationManager = _gameInitializer.getEntityLocationManager();

		_paused = false;

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
