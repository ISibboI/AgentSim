package de.isibboi.agentsim.algorithm.noise;

/**
 * A two dimensional noise function.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Noise {
	/**
	 * Samples the noise function at the given location.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @return The value of the noise function at the given location.
	 */
	double noise(double x, double y);
}
