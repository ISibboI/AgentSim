package de.isibboi.agentsim.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.ui.renderer.Renderer;

/**
 * The view that is displayed when the game was lost.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class GameOverView extends UIView implements ActionListener {
	private final AgentFrame _agentFrame;

	private JButton _restartButton;
	private JButton _quitButton;

	private final JPanel _contentPane;

	/**
	 * Creates a new game over view for the given agent frame.
	 * @param renderer The renderer used to render this view.
	 * @param settings The settings.
	 * @param game The game.
	 * @param agentFrame The agent frame.
	 */
	public GameOverView(final Renderer renderer, final Settings settings, final Game game, final AgentFrame agentFrame) {
		super(renderer, settings);

		_agentFrame = agentFrame;
		_contentPane = new JPanel();

		initUI();
	}

	/**
	 * Creates the UI.
	 */
	private void initUI() {
		_restartButton = new JButton("Restart");
		_restartButton.addActionListener(this);
		_contentPane.add(_restartButton);

		_quitButton = new JButton("Quit");
		_quitButton.addActionListener(this);
		_contentPane.add(_quitButton);
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
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == _restartButton) {
			_agentFrame.receiveRestartGameMessage();
		} else if (e.getSource() == _quitButton) {
			_agentFrame.dispose();
		}
	}

	@Override
	public void update(final Random random, final int tick) throws GameUpdateException {
		// Ignore
	}

	@Override
	public JPanel getJPanel() {
		return _contentPane;
	}
}