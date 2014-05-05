package de.isibboi.agentsim;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple stabilizer for the frame rate.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class FrameRateStabilizer {
	private final Logger _log = LogManager.getLogger(getClass());
	
	private long _lastFrameTime;
	private double _targetFrameTime;
	
	/**
	 * Creates a new {@link FrameRateStabilizer}.
	 * 
	 * @param fps The framerate to stabilize at.
	 */
	public FrameRateStabilizer(final double fps) {
		_targetFrameTime = 1e9 / fps;
		_lastFrameTime = System.nanoTime();
	}
	
	/**
	 * Waits if necessary to keep the required frame rate. Should be called once per frame.
	 */
	public void stabilize() {
		final long currentTime = System.nanoTime();
		
		long waitingTime = (long) (_targetFrameTime - currentTime + _lastFrameTime) / 1_000_000;
		
		if (waitingTime > 0) {
			try {
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				_log.fatal("Waiting interrupted!", e);
			}
		}
	}
}
