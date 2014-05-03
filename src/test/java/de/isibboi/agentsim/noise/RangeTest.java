package de.isibboi.agentsim.noise;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Checks if the noise functions return only values between their global minimum and maximum.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class RangeTest {
	private static final double[][] TEST_RANGES = new double[][] { { 0, 100 }, { 200, 201 }, { -100, 0 }, { -100, 100 } };

	/**
	 * Tests if the scaled simplex noise function returns values in the correct range.
	 */
	@Test
	public void testScaledSimplexNoiseRange() {
		for (int i = 0; i < TEST_RANGES.length; i++) {
			ScaledSimplexNoise noiseMap = new ScaledSimplexNoise(1, 1, TEST_RANGES[i][0], TEST_RANGES[i][1]);

			for (int j = 0; j < 10000; j++) {
				double sample = noiseMap.noise(j % 100, j / 100);

				assertRange(TEST_RANGES[i], sample);
			}
		}
	}

	/**
	 * Checks if the simplex noise function returns values in the correct range.
	 */
	@Test
	public void testSimplexNoiseRange() {
		SimplexNoise noise = new SimplexNoise(49805643105274513L);

		for (int i = 0; i < 10_000; i++) {
			double sample = noise.noise(i / 100, i % 100);
			assertRange(-1, 1, sample);
		}
	}

	/**
	 * Asserts that the given sample is in the given range.
	 * @param range The range. {@code range[0]} is the minimum, {@code range[1]} is the maximum.
	 * @param sample The sample.
	 */
	private void assertRange(final double[] range, final double sample) {
		assertRange(range[0], range[1], sample);
	}

	/**
	 * Asserts that the given sample is in the given range.
	 * @param min The lower bound of the range.
	 * @param max The upper bound of the range.
	 * @param sample The sample.
	 */
	private void assertRange(final double min, final double max, final double sample) {
		assertTrue("Sample out of range [" + min + ", " + max + "]: " + sample, sample >= min && sample <= max);
	}
}
