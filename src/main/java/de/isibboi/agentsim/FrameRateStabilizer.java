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
	private long _lastUpdateTime;
	private int _updatesSinceLastFrame;
	private final long _targetFrameTime;
	private final long _targetUpdateTime;
	private final int _maximumUpdatesPerFrame = 4;

	/**
	 * Creates a new {@link FrameRateStabilizer}.
	 * 
	 * @param framesPerSecond the framerate to stabilize at.
	 * @param updatesPerSecond the target amount of updates per second.
	 */
	public FrameRateStabilizer(final double framesPerSecond, final double updatesPerSecond) {
		_targetFrameTime = (long) (1e9 / framesPerSecond);
		_targetUpdateTime = (long) (1e9 / updatesPerSecond);
		_lastFrameTime = System.nanoTime();
		_lastUpdateTime = System.nanoTime();
	}

	/**
	 * Waits if necessary to keep the required frame rate. Should be called once per frame.
	 * 
	 * @return true if a new frame should be rendered, false if the game should be updated.
	 */
	public boolean stabilize() {
		final long currentTime = System.nanoTime();

		//		if (_updatesSinceLastFrame == 0) {
		//			return update(currentTime);
		//		}
		//		
		//		if (_updatesSinceLastFrame < _maximumUpdatesPerFrame) {
		//			if ()
		//		}
		
		long waitingTime = (long) (_targetFrameTime - (currentTime - _lastFrameTime));

		if (waitingTime > 0) {
			try {
				Thread.sleep(waitingTime / 1_000_000, (int) (waitingTime % 1_000_000));
			} catch (InterruptedException e) {
				_log.fatal("Waiting interrupted!", e);
			}
		}

		_lastFrameTime = currentTime + waitingTime;
		return false;
	}

	/**
	 * Counts the game updates.
	 * 
	 * @param time the current time.
	 * @return false.
	 */
	private boolean update(final long time) {
		_lastUpdateTime = time;
		_updatesSinceLastFrame++;
		return false;
	}

	/**
	 * Resets the game updates.
	 * 
	 * @param time the current time.
	 * @return true.
	 */
	private boolean render(final long time) {
		_lastFrameTime = time;
		_updatesSinceLastFrame = 0;
		return true;
	}
}
