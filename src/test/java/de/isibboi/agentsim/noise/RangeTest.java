package de.isibboi.agentsim.noise;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class RangeTest {
	private static final double[][] testRanges = new double[][] { { 0, 100 }, { 200, 201 }, { -100, 0 }, { -100, 100 } };

	@Test
	public void testNoiseMapRange() {
		for (int i = 0; i < testRanges.length; i++) {
			NoiseMap noiseMap = new NoiseMap(1, 1, testRanges[i][0], testRanges[i][1]);

			for (int j = 0; j < 10000; j++) {
				double sample = noiseMap.noise(j % 100, j / 100);

				assertRange(testRanges[i], sample);
			}
		}
	}
	
	@Test
	public void testSimplexNoiseRange() {
		SimplexNoise noise = new SimplexNoise(49805643105274513L);
		
		for (int i = 0; i < 10_000; i++) {
			double sample = noise.noise(i / 100, i % 100);
			assertRange(-1, 1, sample);
		}
	}

	private void assertRange(double[] range, double sample) {
		assertRange(range[0], range[1], sample);
	}

	private void assertRange(double min, double max, double sample) {
		assertTrue("Sample out of range [" + min + ", " + max + "]: " + sample, sample >= min && sample <= max);
	}
}
