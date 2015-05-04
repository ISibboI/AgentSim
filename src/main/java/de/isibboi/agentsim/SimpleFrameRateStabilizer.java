package de.isibboi.agentsim;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple stabilizer for the frame rate.
 * Does not calculate the correct transition.
 * {@link #getTransition()} always returns 1.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class SimpleFrameRateStabilizer implements FrameRateStabilizer {
	private final Logger _log = LogManager.getLogger(getClass());

	private long _lastFrameTime;
	private double _updatesBeforeNextFrame;
	private final long _targetFrameTime;
	private final double _targetUpdatesPerFrame;
	private final int _maxUpdatesPerFrame;

	/**
	 * Creates a new {@link SimpleFrameRateStabilizer}.
	 * 
	 * @param framesPerSecond The frame rate to stabilize at.
	 * @param updatesPerSecond The target amount of updates per second.
	 * @param maxUpdatesPerFrame The maximum amount of updates per frame.
	 */
	public SimpleFrameRateStabilizer(final double framesPerSecond, final double updatesPerSecond, final int maxUpdatesPerFrame) {
		_targetFrameTime = (long) (1e9 / framesPerSecond);
		_targetUpdatesPerFrame = updatesPerSecond / framesPerSecond;
		_maxUpdatesPerFrame = maxUpdatesPerFrame;

		reset();
	}

	@Override
	public boolean stabilize() {
		final long currentTime = System.nanoTime();

		if (_updatesBeforeNextFrame >= 1) {
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
	 * @return False.
	 */
	protected boolean update() {
		_updatesBeforeNextFrame--;
		return false;
	}

	/**
	 * Resets the game updates.
	 * 
	 * @param normalizedFrameTime The frame time. 1.0 means the frame took exactly as long as it should, values greater than one mean it took longer.
	 * @return True.
	 */
	protected boolean render(final double normalizedFrameTime) {
		_updatesBeforeNextFrame += _targetUpdatesPerFrame * (Math.max(1.0, normalizedFrameTime));

		if (_updatesBeforeNextFrame > _maxUpdatesPerFrame) {
			_updatesBeforeNextFrame = _maxUpdatesPerFrame;
		}

		return true;
	}

	/**
	 * Resets the stabilizer. This should e.g. be used after pausing the rendering.
	 */
	public void reset() {
		_lastFrameTime = System.nanoTime();
	}

	@Override
	public double getTransition() {
		return 1; // No transition when using fast updates.
	}

	/**
	 * Returns the amount of updates that will be made before the next frame is rendered.
	 * This can be lower than one, if there are more than one renders per update.
	 * @return The amount of updates before the next frame is rendered.
	 */
	protected double getUpdatesBeforeNextFrame() {
		return _updatesBeforeNextFrame;
	}
}
