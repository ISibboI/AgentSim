package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.awt.event.WindowListener;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.MapGenerator;

/**
 * Contains all the game data.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class AgentFrame {
	private final Logger _log = LogManager.getLogger(getClass());

	private final DrawFrame _drawFrame;
	private final Settings _settings;

	private GameMap _map;
	private final UI _ui;

	private final Random _random = new Random();

	/**
	 * Creates a new AgentFrame with the given settings.
	 * @param settings The settings.
	 */
	public AgentFrame(final Settings settings) {
		this._settings = settings;

		_drawFrame = new DrawFrame(
				"Agent Sim version " + Environment.VERSION,
				settings.getInt(Settings.UI_WIDTH),
				settings.getInt(Settings.UI_HEIGHT),
				settings.getInt(Settings.GAME_SCALE));

		MapGenerator mapGenerator = new MapGenerator(_settings);
		_map = mapGenerator.generateMap();

		_ui = new UI(new DefaultRenderer(_drawFrame, _settings), _settings, _map);
		_drawFrame.addMouseListener(_ui);
	}

	/**
	 * Updates all entities.
	 */
	public void update() {
		_map.update(_random);
	}

	/**
	 * Renders the map and all entities.
	 */
	public void render() {
		Graphics2D g = _drawFrame.startRender();

		_map.draw(g);

		_drawFrame.switchToUIRender();
		_ui.draw(g);

		_drawFrame.stopRender();
	}

	/**
	 * Spawns {@code amount} goblins.
	 * @param amount The amount of goblins to spawn.
	 */
	public void spawnGoblins(final int amount) {
		_map.spawnGoblins(amount);
	}

	/**
	 * Adds a window listener to the underlying {@link DrawFrame}.
	 * @param listener The listener.
	 */
	public void addWindowListener(final WindowListener listener) {
		_drawFrame.addWindowListener(listener);
	}

	/**
	 * Disposes the underlying {@link DrawFrame}.
	 */
	public void dispose() {
		_drawFrame.dispose();
		_log.debug("Disposed frame");
	}
}
