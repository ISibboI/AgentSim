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
 * Contains all the game data. Creates and manages the views.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class AgentFrame implements GameStatusMessageListener {
	private final Logger _log = LogManager.getLogger(getClass());

	private final DrawFrame _drawFrame;
	private final Settings _settings;

	private Game _game;
	private View _view;
	private final View _gameUIView;
	private final View _gameOverView;
	private int _tick;
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
				settings.getInt(Settings.UI_X_POS),
				settings.getInt(Settings.UI_Y_POS),
				settings.getInt(Settings.GAME_SCALE));

		_game = new Game(new DefaultGameInitializer(), settings, this);

		Renderer renderer = new DefaultRenderer(_drawFrame, _settings);
		_mouseEventTranslator = new MouseEventTranslator();

		_gameUIView = new GameUIView(renderer, _settings, _game, this);
		_gameOverView = new GameOverView(renderer, _settings, _game, this);
		switchView(_gameUIView);
		_view = _gameUIView;
		_drawFrame.getContentPane().addMouseListener(_mouseEventTranslator);
		_drawFrame.getContentPane().addMouseMotionListener(_mouseEventTranslator);

		start();
	}

	/**
	 * Switches to the given view.
	 * 
	 * @param view The new view.
	 */
	private void switchView(final View view) {
		_mouseEventTranslator.removeUIMouseInputListener(_view);

		if (_view != null) {
			_view.deactivate();
		}

		_view = view;
		_view.activate();
		_mouseEventTranslator.addUIMouseInputListener(_view);
	}

	/**
	 * Restarts the game.
	 */
	public void restart() {
		_game.restart();
		_tick = 0;
	}

	/**
	 * Starts the game.
	 */
	public void start() {
		_game.start();
		_tick = 0;
	}

	/**
	 * Updates all entities.
	 */
	public void update() {
		try {
			_game.update(_random, _tick);
			_view.update(_random, _tick);
			_tick++;
		} catch (GameUpdateException e) {
			_log.error("Could not update game!", e);
		}
	}

	/**
	 * Renders the map and all entities.
	 */
	public void render() {
		if (!_drawFrame.isVisible()) {
			return;
		}

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
		_settings.set(Settings.UI_X_POS, _drawFrame.getLocationOnScreen().x);
		_settings.set(Settings.UI_Y_POS, _drawFrame.getLocationOnScreen().y);

		_drawFrame.dispose();
		_log.debug("Frame disposed");
	}

	@Override
	public void receiveGameOverMessage(final String message) {
		switchView(_gameOverView);
	}

	@Override
	public void receiveRestartGameMessage() {
		switchView(_gameUIView);
		_game.restart();
	}
}
