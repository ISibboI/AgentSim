package de.isibboi.agentsim.ui.meter;

import java.util.Queue;

import com.google.common.collect.EvictingQueue;

/**
 * A meter that measures the frequency it is update with.
 * The measurement unit is s^-1.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class FrequencyMeter extends ExponentialAverageMeter {
	private final Queue<Long> _buffer;

	/**
	 * Creates a new frequency meter.
	 * The meter has an internal buffer that buffers the time values. A large buffer has a high resistance against peaks, but a lower reaction time. 
	 * 
	 * @param initialValue the initial value of the meter.
	 * @param factor the factor used to update the value. Has to be between 0 and 1. 0 means, nothing is ever changed, 1 means, the new value is copied on update.
	 * @param bufferCapacity the capacity of the internal buffer.
	 */
	public FrequencyMeter(final double initialValue, final double factor, final int bufferCapacity) {
		super(initialValue, factor);

		_buffer = EvictingQueue.create(bufferCapacity);
	}

	@Override
	public void update() {
		long time = System.nanoTime();

		if (_buffer.size() != 0) {
			double delta = time - _buffer.peek();
			delta /= 1e9 * _buffer.size();
			updateValue(1 / delta);
		}

		_buffer.add(time);
	}
}