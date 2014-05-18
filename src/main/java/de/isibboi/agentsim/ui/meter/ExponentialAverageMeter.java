package de.isibboi.agentsim.ui.meter;

/**
 * An abstract meter that calculates the exponential average of the measured values.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public abstract class ExponentialAverageMeter implements Meter {
	private double _value;
	private final double _factor;

	/**
	 * Creates a new exponential averaged meter.
	 * @param initialValue The initial value of the meter.
	 * @param factor The factor used to update the value. Has to be between 0 and 1. 0 means, nothing is ever changed, 1 means, the new value is copied on update.
	 */
	public ExponentialAverageMeter(final double initialValue, final double factor) {
		_value = initialValue;
		_factor = factor;
	}
	
	/**
	 * Updates the value of the meter.
	 * @param value The new value.
	 */
	protected void updateValue(final double value) {
		_value = value * _factor + _value * (1 - _factor);
	}

	@Override
	public double getValue() {
		return _value;
	}

}
