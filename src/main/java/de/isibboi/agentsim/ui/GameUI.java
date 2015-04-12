package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.component.UIButton;
import de.isibboi.agentsim.ui.component.UINumberLabel;
import de.isibboi.agentsim.ui.event.MouseButton;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;
import de.isibboi.agentsim.ui.event.UserActionEvent;
import de.isibboi.agentsim.ui.meter.FrequencyMeter;

/**
 * Represents the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GameUI implements View, Updateable {
	private final Logger _log = LogManager.getLogger(getClass());

	private final Settings _settings;
	private Game _game;
	private final AgentFrame _agentFrame;

	private int _width;
	private int _height;

	private final FrequencyMeter _frameRateMeter;
	private final FrequencyMeter _updateRateMeter;

	private final Renderer _renderer;
	private final Collection<Drawable> _drawables = new ArrayList<>();
	private final Collection<UIMouseInputListener> _mouseListeners = new ArrayList<>();

	private UINumberLabel _frameRateLabel;
	private UINumberLabel _updateRateLabel;
	private UINumberLabel _entityCountLabel;

	private UIButton _settingsButton;
	private UIButton _renderEntitiesButton;
	private UIButton _restartButton;
	private UIButton _pauseButton;

	private UISettingsFrame _settingsFrame;

	private boolean _renderEntities = true;

	/**
	 * Creates a new ui with the given settings.
	 * 
	 * @param renderer the renderer used to draw the ui.
	 * @param settings the settings.
	 * @param game the game map.
	 * @param agentFrame the game main class.
	 */
	public GameUI(final Renderer renderer, final Settings settings, final Game game, final AgentFrame agentFrame) {
		_settings = settings;
		_renderer = renderer;
		_game = game;
		_agentFrame = agentFrame;

		_width = settings.getInt(Settings.UI_WIDTH);
		_height = settings.getInt(Settings.UI_HEIGHT);

		_frameRateMeter = new FrequencyMeter(settings.getInt(Settings.CORE_TARGET_FRAME_RATE), 0.1, 8);
		_updateRateMeter = new FrequencyMeter(settings.getInt(Settings.CORE_TARGET_UPDATE_RATE), 0.03, 8);

		initUI();
	}

	/**
	 * Creates the ui.
	 */
	private void initUI() {
		_frameRateLabel = new UINumberLabel(_renderer, new Point(_width - 260, 10), 250, "Framerate: ", "", 0, 0);
		_drawables.add(_frameRateLabel);

		_updateRateLabel = new UINumberLabel(_renderer, new Point(_width - 260, 50), 250, "Update rate: ", "", 0, 0);
		_drawables.add(_updateRateLabel);

		_entityCountLabel = new UINumberLabel(_renderer, new Point(_width - 260, 90), 250, "Entity count: ", "", 0, _settings.getInt(Settings.GAME_INITIAL_GOBLIN_COUNT));
		_drawables.add(_entityCountLabel);

		_settingsButton = new UIButton(_renderer, new Point(_width - 260, 130), 250, "Settings", this);
		_drawables.add(_settingsButton);
		_mouseListeners.add(_settingsButton);

		_renderEntitiesButton = new UIButton(_renderer, new Point(_width - 260, 170), 250, "Toggle entities", this);
		_drawables.add(_renderEntitiesButton);
		_mouseListeners.add(_renderEntitiesButton);

		_restartButton = new UIButton(_renderer, new Point(_width - 260, 210), 250, "Restart", this);
		_drawables.add(_restartButton);
		_mouseListeners.add(_restartButton);

		_pauseButton = new UIButton(_renderer, new Point(_width - 260, 250), 250, "Pause", this);
		_drawables.add(_pauseButton);
		_mouseListeners.add(_pauseButton);
	}

	@Override
	public void drawScaledContent(final Graphics2D g) {
		// Draw map.
		_game.getMap().draw(g);

		// Draw entities.
		if (_renderEntities) {
			_game.getEntities().draw(g);
		}
	};

	@Override
	public void drawUnscaledContent(final Graphics2D g) {
		// Measure frame rate.
		_frameRateMeter.update();
		_frameRateLabel.setValue(_frameRateMeter.getValue());

		// Measure update rate.
		_updateRateLabel.setValue(_updateRateMeter.getValue());

		// Measure entity count.
		_entityCountLabel.setValue(_game.getEntities().size());

		_renderer.setGraphics(g);
		for (Drawable d : _drawables) {
			d.draw(g);
		}
	}

	@Override
	public void update(final Random random) throws GameUpdateException {
		_updateRateMeter.update();
	}

	@Override
	public void mouseMoved(final Point oldPosition, final Point newPosition) {
		for (UIMouseInputListener listener : _mouseListeners) {
			listener.mouseMoved(oldPosition, newPosition);
		}
	}

	@Override
	public void mouseClicked(final Point position, final MouseButton button, final boolean buttonDown) {
		for (UIMouseInputListener listener : _mouseListeners) {
			listener.mouseClicked(position, button, buttonDown);
		}
	}

	@Override
	public void userAction(final UserActionEvent e) {
		_log.debug("Received UserActionEvent");

		if (e.getSource() == _settingsButton && _settingsFrame == null) {
			_settingsFrame = new UISettingsFrame(_settings, this);
		} else if (e.getSource() == _settingsFrame) {
			_settingsFrame = null;
		} else if (e.getSource() == _renderEntitiesButton) {
			_renderEntities = !_renderEntities;
		} else if (e.getSource() == _restartButton) {
			_agentFrame.restart();
		} else if (e.getSource() == _pauseButton) {
			_game.setPaused(!_game.isPaused());
		}
	}

	@Override
	public void activate() {
		// Ignore
	}

	@Override
	public void deactivate() {
		// Ignore
	}

	@Override
	public void setGame(final Game game) {
		_game = game;
	}
}