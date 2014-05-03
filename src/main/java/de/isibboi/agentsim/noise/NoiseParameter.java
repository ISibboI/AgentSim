package de.isibboi.agentsim.noise;

import java.util.Random;

public class NoiseParameter {
	private double cellWidth;
	private double cellHeight;
	private double min;
	private double max;
	private long seed;

	public NoiseParameter(double cellWidth, double cellHeight, double min, double max) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.min = min;
		this.max = max;
		this.seed = new Random().nextLong();
	}

	public double getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(double cellWidth) {
		this.cellWidth = cellWidth;
	}

	public double getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(double cellHeight) {
		this.cellHeight = cellHeight;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
}