package de.isibboi.agentsim;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.ui.AgentFrame;

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
		sim.run(); // Don't need to start a new thread here, as we don't need the main thread anymore.
	}

	private final Logger _log = LogManager.getLogger(getClass());

	private final AgentFrame _frame;
	private final Settings _settings;

	private volatile boolean _exit;

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
		FrameRateStabilizer frameRateStabilizer = new SlowFrameRateStabilizer(
				_settings.getInt(Settings.CORE_TARGET_FRAME_RATE),
				_settings.getInt(Settings.CORE_TARGET_UPDATE_RATE),
				_settings.getInt(Settings.CORE_MAX_UPDATES_PER_FRAME));

		while (!_exit) {
			frameRateStabilizer.setGamePaused(_frame.isGamePaused());

			if (frameRateStabilizer.stabilize()) {
				_frame.render(frameRateStabilizer.getTransition());
			} else {
				_frame.update();
			}

		}

		_frame.dispose();
		_settings.close();
	}

	@Override
	public void windowOpened(final WindowEvent e) {
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		_log.info("Exit requested");
		_exit = true;
	}

	@Override
	public void windowClosed(final WindowEvent e) {
	}

	@Override
	public void windowIconified(final WindowEvent e) {
	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
	}

	@Override
	public void windowActivated(final WindowEvent e) {
	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
	}
}
