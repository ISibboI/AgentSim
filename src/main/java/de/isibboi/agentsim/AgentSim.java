package de.isibboi.agentsim;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.Field;
import java.util.Set;

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

		// Set of current Threads
		Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();

		// Iterate over set to find yours
		for (Thread thread : setOfThread) {
			if (thread.getName().equals("Thread-2")) {
				thread.interrupt();

				_log.info("Interrupted Thread-2");

				for (StackTraceElement e : thread.getStackTrace()) {
					System.out.println(e);
				}

				_log.info("Printed stack trace of Thread-2");

				_log.info("Thread-2 is " + (thread.isAlive() ? "" : "NOT ") + "alive.");

				try {
					System.out.println("======= Threads fields =======");

					for (Field field : thread.getClass().getDeclaredFields()) {
						field.setAccessible(true);
						System.out.println("    " + field.getName() + ": " + field.get(thread));
					}

					System.out.println("Threads class: " + thread.getClass());
					System.out.println("Thread group: " + thread.getThreadGroup());
					System.out.println("Main thread thread group: " + Thread.currentThread().getThreadGroup());
					System.out.println("Thread tid: " + thread.getId());
				} catch (SecurityException | IllegalAccessException e1) {
					e1.printStackTrace();
				}

				break;
			}
		}
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
		_log.info("Frame closed");
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
