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
	private double _updatesBeforeNextFrame;
	private final long _targetFrameTime;
	private final double _targetUpdatesPerFrame;
	private final int _maximumUpdatesPerFrame = 4;

	/**
	 * Creates a new {@link FrameRateStabilizer}.
	 * 
	 * @param framesPerSecond the framerate to stabilize at.
	 * @param updatesPerSecond the target amount of updates per second.
	 */
	public FrameRateStabilizer(final double framesPerSecond, final double updatesPerSecond) {
		_targetFrameTime = (long) (1e9 / framesPerSecond);
		_targetUpdatesPerFrame = updatesPerSecond / framesPerSecond;
		_lastFrameTime = System.nanoTime();
	}

	/**
	 * Waits if necessary to keep the required frame rate. Should be called once per frame.
	 * 
	 * @return true if a new frame should be rendered, false if the game should be updated.
	 */
	public boolean stabilize() {
		final long currentTime = System.nanoTime();

		if (_updatesBeforeNextFrame > 1.0001) {
			return update();
		} else {
			long waitingTime = (long) (_targetFrameTime - (currentTime - _lastFrameTime));

			if (waitingTime > 0) {
				try {
					Thread.sleep(waitingTime / 1_000_000, (int) (waitingTime % 1_000_000));
				} catch (InterruptedException e) {
					_log.fatal("Waiting interrupted!", e);
				}
			}

			double normalizedFrameTime = 1.0 * (currentTime - _lastFrameTime) / _targetFrameTime;
			_lastFrameTime = System.nanoTime();
			return render(normalizedFrameTime);
		}
	}

	/**
	 * Counts the game updates.
	 * 
	 * @return false.
	 */
	private boolean update() {
		_updatesBeforeNextFrame--;
		return false;
	}

	/**
	 * Resets the game updates.
	 * 
	 * @param normalizedFrameTime the frame time. 1.0 means the frame took exactly as long as it should, values greater than one mean it took longer.
	 * @return true.
	 */
	private boolean render(final double normalizedFrameTime) {
		_updatesBeforeNextFrame += _targetUpdatesPerFrame * (Math.max(1.0, normalizedFrameTime));
		
		if (_updatesBeforeNextFrame > _maximumUpdatesPerFrame) {
			_updatesBeforeNextFrame = _maximumUpdatesPerFrame;
		}

		return true;
	}
	
	/**
	 * Resets the stabilizer. This should e.g. be used after pausing the rendering.
	 */
	public void reset() {
		_lastFrameTime = System.nanoTime();
	}
}
