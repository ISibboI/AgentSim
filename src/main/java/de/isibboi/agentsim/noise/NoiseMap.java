package de.isibboi.agentsim.noise;

import java.util.Random;

public class NoiseMap extends SimplexNoise implements Noise {
	private double cellWidth;
	private double cellHeight;
	private double min;
	private double max;
	
	public NoiseMap(long seed, double cellWidth, double cellHeight, double min, double max) {
		super(seed);
		
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.min = min;
		this.max = max;
	}

	public NoiseMap(NoiseParameter noiseParameter) {
		super(noiseParameter.getSeed());

		this.cellWidth = noiseParameter.getCellWidth();
		this.cellHeight = noiseParameter.getCellHeight();
		this.min = noiseParameter.getMin();
		this.max = noiseParameter.getMax();
	}

	public NoiseMap(double cellWidth, double cellHeight, double min, double max) {
		super(new Random().nextLong());

		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.min = min;
		this.max = max;
	}

	public double noise(double x, double y) {
		double result = super.noise(x / cellWidth, y / cellHeight);
		
		double halfTargetIntervalSize = (max - min) / 2;
		
		result *= halfTargetIntervalSize;
		result += halfTargetIntervalSize + min;
		
		return result;
	}
}