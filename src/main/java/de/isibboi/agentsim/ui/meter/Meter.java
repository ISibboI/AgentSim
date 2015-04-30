package de.isibboi.agentsim.ui.meter;

/**
 * A meter to measure stuff.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Meter {
	/**
	 * Returns the current value of the meter.
	 * @return The current value of the meter.
	 */
	double getValue();

	/**
	 * Updates the value of the meter.
	 */
	void update();

	/**
	 * Resets the value of the meter.
	 */
	void reset();
}
