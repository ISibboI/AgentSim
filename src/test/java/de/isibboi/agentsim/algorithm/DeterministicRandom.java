package de.isibboi.agentsim.algorithm;

import java.util.Random;

/**
 * A deterministic random class.
 * The results of the random methods can be preselected.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class DeterministicRandom extends Random {
	private static final long serialVersionUID = 1L;
	private int _nextInt;

	/**
	 * Sets the value to be returned by the {@link #nextInt()} and {@link #nextInt(int)} methods.
	 * @param nextInt The value.
	 */
	public void setNextInt(final int nextInt) {
		_nextInt = nextInt;
	}

	@Override
	public int nextInt(final int n) {
		if (_nextInt >= n) {
			throw new IllegalStateException();
		}

		return _nextInt;
	}

	@Override
	public int nextInt() {
		return _nextInt;
	}
}