package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.component.UIButton;
import de.isibboi.agentsim.ui.component.UINumberLabel;
import de.isibboi.agentsim.ui.event.MouseButton;
import de.isibboi.agentsim.ui.event.UIActionListener;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;
import de.isibboi.agentsim.ui.event.UserActionEvent;
import de.isibboi.agentsim.ui.meter.FrequencyMeter;

/**
 * Represents the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class UI implements Drawable, Updateable, UIMouseInputListener, UIActionListener {
	private final Logger _log = LogManager.getLogger(getClass());
	
	private final Settings _settings;
	private final GameMap _map;

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
	
	private UISettingsFrame _settingsFrame;

	/**
	 * Creates a new ui with the given settings.
	 * 
	 * @param renderer The renderer used to draw the ui.
	 * @param settings The settings.
	 * @param map The game map.
	 */
	public UI(final Renderer renderer, final Settings settings, final GameMap map) {
		_settings = settings;
		_renderer = renderer;
		_map = map;

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
		_frameRateLabel = new UINumberLabel(_renderer, new Point(_width - 270, 10), 250, "Framerate: ", "", 1, 0);
		_drawables.add(_frameRateLabel);

		_updateRateLabel = new UINumberLabel(_renderer, new Point(_width - 270, 50), 250, "Update rate: ", "", 1, 0);
		_drawables.add(_updateRateLabel);

		_entityCountLabel = new UINumberLabel(_renderer, new Point(_width - 270, 90), 250, "Entity count: ", "", 0, _settings.getInt(Settings.GAME_INITIAL_GOBLIN_COUNT));
		_drawables.add(_entityCountLabel);
		
		_settingsButton = new UIButton(_renderer, new Point(_width - 270, 130), 250, "Settings", this);
		_drawables.add(_settingsButton);
		_mouseListeners.add(_settingsButton);
	}

	@Override
	public void draw(final Graphics2D g) {
		// Measure frame rate.
		_frameRateMeter.update();
		_frameRateLabel.setValue(_frameRateMeter.getValue());

		// Measure update rate.
		_updateRateLabel.setValue(_updateRateMeter.getValue());

		// Measure entity count.
		_entityCountLabel.setValue(_map.getEntityCount());

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
	}
}