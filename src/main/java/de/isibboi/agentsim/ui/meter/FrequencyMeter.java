package de.isibboi.agentsim.ui.meter;

/**
 * A meter that measures the frequency it is update with.
 * The measurement unit is s^-1.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class FrequencyMeter extends ExponentialAverageMeter {
	private long _lastUpdate = -1;
	
	/**
	 * Creates a new frequency meter.
	 * @param initialValue The initial value of the meter.
	 * @param factor The factor used to update the value. Has to be between 0 and 1. 0 means, nothing is ever changed, 1 means, the new value is copied on update.
	 */
	public FrequencyMeter(final double initialValue, final double factor) {
		super(initialValue, factor);
	}
	
	@Override
	public void update() {
		long time = System.nanoTime();
		
		if (_lastUpdate != -1) {
			double delta = time - _lastUpdate;
			delta /= 1e9;
			updateValue(1 / delta);
		}
		
		_lastUpdate = time;
	}
}