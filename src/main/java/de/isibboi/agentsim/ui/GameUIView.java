package de.isibboi.agentsim.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.ui.components.NumberLabel;
import de.isibboi.agentsim.ui.meter.FrequencyMeter;
import de.isibboi.agentsim.ui.opengl.GameGLPanel;
import de.isibboi.agentsim.ui.renderer.Renderer;

/**
 * Represents the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class GameUIView extends UIView implements ActionListener {
	private final Logger _log = LogManager.getLogger(getClass());

	private final Settings _settings;
	private final Game _game;
	private final AgentFrame _agentFrame;

	private final FrequencyMeter _frameRateMeter;
	private final FrequencyMeter _updateRateMeter;

	private NumberLabel _frameRateLabel;
	private NumberLabel _updateRateLabel;
	private NumberLabel _entityCountLabel;

	private JButton _settingsButton;
	private JButton _renderEntitiesButton;
	private JButton _restartButton;
	private JButton _pauseButton;

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
		GameGLPanel glPanel = new GameGLPanel();
		JPanel uiPanel = new JPanel();

		_contentPane.setLayout(new GridBagLayout());
		final GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		_contentPane.add(glPanel.getJPanel(), gbc);
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		_contentPane.add(uiPanel, gbc);

		uiPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 0;
		gbc.insets = new Insets(4, 4, 4, 4);

		_frameRateLabel = new NumberLabel("Framerate: ", new DecimalFormat("###,###"), "/s");
		gbc.gridy = 0;
		uiPanel.add(_frameRateLabel, gbc);

		_updateRateLabel = new NumberLabel("Update rate: ", new DecimalFormat("###,###"), "/s");
		gbc.gridy = 1;
		uiPanel.add(_updateRateLabel, gbc);

		_entityCountLabel = new NumberLabel("Entity count: ", new DecimalFormat("###,###"));
		gbc.gridy = 2;
		uiPanel.add(_entityCountLabel, gbc);

		_settingsButton = new JButton("Settings");
		_settingsButton.addActionListener(this);
		gbc.gridy = 3;
		uiPanel.add(_settingsButton, gbc);

		_renderEntitiesButton = new JButton("Toggle entities");
		_renderEntitiesButton.addActionListener(this);
		gbc.gridy = 4;
		uiPanel.add(_renderEntitiesButton, gbc);

		_restartButton = new JButton("Restart");
		_restartButton.addActionListener(this);
		gbc.gridy = 5;
		uiPanel.add(_restartButton, gbc);

		_pauseButton = new JButton("Pause");
		_pauseButton.addActionListener(this);
		gbc.gridy = 6;
		uiPanel.add(_pauseButton, gbc);
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
		_updateRateLabel.setNumber(_updateRateMeter.getValue());
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		_log.debug("Received ActionEvent: " + e);

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
