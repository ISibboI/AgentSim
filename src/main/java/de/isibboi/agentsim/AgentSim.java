package de.isibboi.agentsim;

import de.isibboi.agentsim.ui.AgentFrame;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

/**
 * The AgentSim main class. Executes the game.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class AgentSim implements Runnable, WindowListener {
	/**
	 * Starts the game.
	 * @param args Unused.
	 */
	public static void main(final String[] args) {
		AgentSim sim = new AgentSim();
		sim.run();
	}

	private final Logger _log = LogManager.getLogger(getClass());

	private final AgentFrame _frame;
	private final Settings _settings;

	private boolean _exit;

	/**
	 * Constructs the game.
	 */
	public AgentSim() {
		_settings = new Settings("agentsim.settings");

		_frame = new AgentFrame(_settings);
		_frame.addWindowListener(this);
	}

	@Override
	public void run() {
		// Spawn initial entities
		_frame.spawnGoblins(_settings.getInt(Settings.GAME_INITIAL_GOBLIN_COUNT));
		
		FrameRateStabilizer frameRateStabilizer = new FrameRateStabilizer(_settings.getInt(Settings.UI_TARGET_FRAMERATE));
		
		while (!_exit) {
			_frame.update();
			_frame.render();
			
			frameRateStabilizer.stabilize();
		}
		
		_frame.dispose();
		_settings.close();
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		_log.info("Exit requested");
		_exit = true;
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
