package de.isibboi.agentsim.noise;

public class CombinedNoise implements Noise {
	private Noise[] noises;

	public CombinedNoise(final Noise[] noises) {
		this.noises = noises;
	}

	@Override
	public double noise(final double x, final double y) {
		double result = 0;

		for (Noise noise : noises) {
			result += noise.noise(x, y);
		}

		return result;
	}
}