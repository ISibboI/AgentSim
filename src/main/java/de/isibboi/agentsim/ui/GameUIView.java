package de.isibboi.agentsim.ui;

import java.awt.BorderLayout;
import java.util.Random;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.component.UIButton;
import de.isibboi.agentsim.ui.component.UINumberLabel;
import de.isibboi.agentsim.ui.event.UserActionEvent;
import de.isibboi.agentsim.ui.meter.FrequencyMeter;
import de.isibboi.agentsim.ui.opengl.GameGLPanel;

/**
 * Represents the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GameUIView extends UIView {
	private final Logger _log = LogManager.getLogger(getClass());

	private final Settings _settings;
	private final Game _game;
	private final AgentFrame _agentFrame;

	private final FrequencyMeter _frameRateMeter;
	private final FrequencyMeter _updateRateMeter;

	private UINumberLabel _frameRateLabel;
	private UINumberLabel _updateRateLabel;
	private UINumberLabel _entityCountLabel;

	private UIButton _settingsButton;
	private UIButton _renderEntitiesButton;
	private UIButton _restartButton;
	private UIButton _pauseButton;

	private UISettingsFrame _settingsFrame;

	private MapRenderMode _mapRenderMode;
	private EntitySelectionManager _entitySelectionManager;

	private boolean _renderEntities = true;

	private final JPanel _contentPane;

	/**
	 * Creates a new ui with the given settings.
	 * 
	 * @param renderer the renderer used to draw the UI.
	 * @param settings the settings.
	 * @param game the game map.
	 * @param agentFrame the game main class.
	 */
	public GameUIView(final Renderer renderer, final Settings settings, final Game game, final AgentFrame agentFrame) {
		super(renderer, settings);

		_settings = settings;
		_game = game;
		_agentFrame = agentFrame;

		_frameRateMeter = new FrequencyMeter(settings.getInt(Settings.CORE_TARGET_FRAME_RATE), 0.1, 8);
		_updateRateMeter = new FrequencyMeter(settings.getInt(Settings.CORE_TARGET_UPDATE_RATE), 0.03, 8);

		_mapRenderMode = new DrawAllMapRenderMode();
		_entitySelectionManager = new EntitySelectionManager(_game, _settings, getRenderer());
		addMouseListener(_entitySelectionManager);

		_contentPane = new JPanel();

		initUI();
	}

	/**
	 * Creates the UI.
	 */
	private void initUI() {
		_frameRateLabel = new UINumberLabel(getRenderer(), new Point(getWidth() - 260, 10), 250, "Framerate: ", "", 0, 0);
		add(_frameRateLabel);

		_updateRateLabel = new UINumberLabel(getRenderer(), new Point(getWidth() - 260, 50), 250, "Update rate: ", "", 0, 0);
		add(_updateRateLabel);

		_entityCountLabel = new UINumberLabel(getRenderer(), new Point(getWidth() - 260, 90), 250, "Entity count: ", "", 0,
				_settings.getInt(Settings.GAME_INITIAL_GOBLIN_COUNT));
		add(_entityCountLabel);

		_settingsButton = new UIButton(getRenderer(), new Point(getWidth() - 260, 130), 250, "Settings", this);
		add(_settingsButton);
		addMouseListener(_settingsButton);

		_renderEntitiesButton = new UIButton(getRenderer(), new Point(getWidth() - 260, 170), 250, "Toggle entities", this);
		add(_renderEntitiesButton);
		addMouseListener(_renderEntitiesButton);

		_restartButton = new UIButton(getRenderer(), new Point(getWidth() - 260, 210), 250, "Restart", this);
		add(_restartButton);
		addMouseListener(_restartButton);

		_pauseButton = new UIButton(getRenderer(), new Point(getWidth() - 260, 250), 250, "Pause", this);
		add(_pauseButton);
		addMouseListener(_pauseButton);

		GameGLPanel glPanel = new GameGLPanel();
		JPanel uiPanel = new JPanel();

		_contentPane.add(glPanel.getJPanel(), BorderLayout.WEST);
		_contentPane.add(uiPanel, BorderLayout.EAST);
	}

	//	@Override
	//	public void drawScaledContent(final Graphics2D g, final double transition) {
	//		_mapRenderMode.drawMap(g, transition, _game.getMap());
	//
	//		if (_renderEntities) {
	//			_mapRenderMode.drawEntities(g, transition, _game.getEntities());
	//		}
	//	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		_updateRateMeter.update();
	}

	@Override
	public void userAction(final UserActionEvent e) {
		_log.debug("Received UserActionEvent");

		if (e.getSource() == _settingsButton && _settingsFrame == null) {
			_settingsFrame = new UISettingsFrame(_settings, this);
		} else if (e.getSource() == _settingsFrame) {
			_settingsFrame.dispose();
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
		_updateRateMeter.reset();
		_frameRateMeter.reset();
	}

	@Override
	public void deactivate() {
		// Ignore
	}

	@Override
	public JPanel getJPanel() {
		return _contentPane;
	}
}
