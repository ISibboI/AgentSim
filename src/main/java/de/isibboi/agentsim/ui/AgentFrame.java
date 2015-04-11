package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.awt.event.WindowListener;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.DefaultGameInitializer;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.ui.event.MouseEventTranslator;

/**
 * Contains all the game data.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class AgentFrame {
	private final Logger _log = LogManager.getLogger(getClass());

	private final DrawFrame _drawFrame;
	private final Settings _settings;

	private Game _game;
	private View _view;
	private final MouseEventTranslator _mouseEventTranslator;

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

		_game = new Game(new DefaultGameInitializer(), settings);

		_view = new GameUI(new DefaultRenderer(_drawFrame, _settings), _settings, _game, this);
		_mouseEventTranslator = new MouseEventTranslator(_view);
		_drawFrame.addMouseListener(_mouseEventTranslator);
		_drawFrame.addMouseMotionListener(_mouseEventTranslator);

		start();
	}

	/**
	 * Restarts the game.
	 */
	public void restart() {
		_game.restart();
	}

	/**
	 * Starts the game.
	 */
	public void start() {
		_game.start();
	}

	/**
	 * Updates all entities.
	 */
	public void update() {
		try {
			_game.update(_random);
		} catch (GameUpdateException e) {
			_log.error("Could not update ui!", e);
		}
	}

	/**
	 * Renders the map and all entities.
	 */
	public void render() {
		Graphics2D g = _drawFrame.startRender();

		_view.drawScaledContent(g);
		_drawFrame.switchToUIRender();
		_view.drawUnscaledContent(g);

		_drawFrame.finishRender();
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
