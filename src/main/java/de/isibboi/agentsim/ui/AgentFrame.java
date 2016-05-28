package de.isibboi.agentsim.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Environment;
import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.DefaultGameInitializer;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.ui.event.MouseEventTranslator;
import de.isibboi.agentsim.ui.renderer.DefaultRenderer;
import de.isibboi.agentsim.ui.renderer.Renderer;

/**
 * Contains all the game data. Creates and manages the views.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class AgentFrame implements GameStatusMessageListener {
	private static final int START_TICK = 0;

	private static final Logger LOG = LogManager.getLogger(AgentFrame.class);

	private final JFrame _frame;
	private final Settings _settings;

	private Game _game;
	private View _view;
	private final View _gameUIView;
	private final View _gameOverView;
	private int _tick;
	private double _transitionFactor;
	private final MouseEventTranslator _mouseEventTranslator;

	private final Random _random = new Random();

	/**
	 * Creates a new AgentFrame with the given settings.
	 * @param settings The settings.
	 */
	public AgentFrame(final Settings settings) {
		this._settings = settings;

		_frame = new JFrame(
				"Agent Sim version " + Environment.VERSION);
		_frame.setLocation(settings.getInt(Settings.UI_X_POS),
				settings.getInt(Settings.UI_Y_POS));

		JPanel contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(settings.getInt(Settings.UI_WIDTH),
				settings.getInt(Settings.UI_HEIGHT)));
		contentPane.setLayout(new GridBagLayout());
		_frame.setContentPane(contentPane);

		_game = new Game(new DefaultGameInitializer(), settings, this);

		Renderer renderer = new DefaultRenderer();
		_mouseEventTranslator = new MouseEventTranslator();

		_gameUIView = new GameUIView(renderer, _settings, _game, this);
		_gameOverView = new GameOverView(renderer, _settings, _game, this);
		switchView(_gameUIView);
		_view = _gameUIView;
		_frame.getContentPane().addMouseListener(_mouseEventTranslator);
		_frame.getContentPane().addMouseMotionListener(_mouseEventTranslator);

		_transitionFactor = 1 / settings.getDouble(Settings.CORE_RENDER_TRANSITION_AMOUNT);

		_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		_frame.pack();
		_frame.setVisible(true);

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

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);

		_frame.getContentPane().removeAll();
		_frame.getContentPane().add(view.getJPanel(), gbc);

		LOG.debug("Switched view to: " + view);
	}

	/**
	 * Restarts the game.
	 */
	public void restart() {
		_game.restart();
		_tick = START_TICK;
	}

	/**
	 * Starts the game.
	 */
	public void start() {
		_game.start();
		_tick = START_TICK;
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
			LOG.error("Could not update game!", e);
		}
	}

	/**
	 * Renders the map and all entities.
	 * The transition parameter determines where the entity is exactly drawn.
	 * For zero, the entity should be drawn at the location from the last update, for one, it should be drawn at the current location.
	 * Values between zero and one should be used for interpolation between old and new location.
	 * 
	 * @param transition A value between zero and one.
	 */
	public void render(final float transition) {
		if (!_frame.isVisible()) {
			return;
		}

		double shortenedTransition = transition * _transitionFactor;

		if (shortenedTransition > 1) {
			shortenedTransition = 1;
		}

		_view.render(_tick, transition);
	}

	/**
	 * Adds a window listener to the underlying {@link DrawFrame}.
	 * @param listener The listener.
	 */
	public void addWindowListener(final WindowListener listener) {
		_frame.addWindowListener(listener);
	}

	/**
	 * Disposes the underlying {@link DrawFrame}.
	 */
	public void dispose() {
		_settings.set(Settings.UI_X_POS, _frame.getLocationOnScreen().x);
		_settings.set(Settings.UI_Y_POS, _frame.getLocationOnScreen().y);

		_game.stop();

		_frame.dispose();
		LOG.debug("Frame disposed");
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

	/**
	 * Returns if the game is paused.
	 * @return True if the game is paused, false otherwise.
	 */
	public boolean isGamePaused() {
		return _game.isPaused();
	}
}
