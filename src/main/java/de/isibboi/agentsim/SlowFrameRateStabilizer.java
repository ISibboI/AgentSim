package de.isibboi.agentsim;

/**
 * A frame rate stabilizer that adds the transition calculation to the {@link SimpleFrameRateStabilizer}.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public class SlowFrameRateStabilizer extends SimpleFrameRateStabilizer {
	private double _transition;

	/**
	 * Creates a new {@link SlowFrameRateStabilizer}.
	 * 
	 * @param framesPerSecond The frame rate to stabilize at.
	 * @param updatesPerSecond The target amount of updates per second.
	 * @param maxUpdatesPerFrame The maximum amount of updates per frame.
	 */
	public SlowFrameRateStabilizer(final double framesPerSecond, final double updatesPerSecond, final int maxUpdatesPerFrame) {
		super(framesPerSecond, updatesPerSecond, maxUpdatesPerFrame);
	}

	@Override
	protected boolean render(final double normalizedFrameTime) {
		boolean result = super.render(normalizedFrameTime);

		_transition = getUpdatesBeforeNextFrame();

		if (_transition > 1) {
			_transition = 1;
		}

		return result;
	}

	@Override
	public double getTransition() {
		return _transition;
	}
}