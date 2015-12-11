package de.isibboi.agentsim.ui;

import java.util.Random;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.Game;
import de.isibboi.agentsim.game.GameUpdateException;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.component.UIButton;
import de.isibboi.agentsim.ui.event.UserActionEvent;

/**
 * The view that is displayed when the game was lost.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class GameOverView extends UIView {
	private final AgentFrame _agentFrame;

	private UIButton _restartButton;
	private UIButton _quitButton;

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

		initUI();
	}

	/**
	 * Creates the UI.
	 */
	private void initUI() {
		Point center = new Point(getWidth() / 2, getHeight() / 2);

		_restartButton = new UIButton(getRenderer(), center.add(new Point(-105, -20)), 100, "Restart", this);
		add(_restartButton);
		addMouseListener(_restartButton);

		_quitButton = new UIButton(getRenderer(), center.add(new Point(5, -20)), 100, "Quit", this);
		add(_quitButton);
		addMouseListener(_quitButton);
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
	public void userAction(final UserActionEvent e) {
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
}