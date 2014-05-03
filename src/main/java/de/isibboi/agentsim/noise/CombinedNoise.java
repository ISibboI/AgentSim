package de.isibboi.agentsim.noise;

public class CombinedNoise implements Noise {
	private Noise[] noises;
	
	public CombinedNoise(Noise[] noises) {
		this.noises = noises;
	}
	
	public double noise(double x, double y) {
		double result = 0;
		
		for (Noise noise : noises) {
			result += noise.noise(x, y);
		}
		
		return result;
	}
}