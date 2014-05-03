package de.isibboi.agentsim.noise;

/**
 * A noise function that just adds up the values of some other noise functions.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class CombinedNoise implements Noise {
	private Noise[] _noises;

	/**
	 * Creates a sum of the given noise functions.
	 * @param noises The noise functions.
	 */
	public CombinedNoise(final Noise[] noises) {
		this._noises = noises;
	}

	@Override
	public double noise(final double x, final double y) {
		double result = 0;

		for (Noise noise : _noises) {
			result += noise.noise(x, y);
		}

		return result;
	}
}