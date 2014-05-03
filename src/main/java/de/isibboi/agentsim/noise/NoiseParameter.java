package de.isibboi.agentsim.noise;

import java.util.Random;

public class NoiseParameter {
	private double cellWidth;
	private double cellHeight;
	private double min;
	private double max;
	private long seed;

	public NoiseParameter(final double cellWidth, final double cellHeight, final double min, final double max) {
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.min = min;
		this.max = max;
		this.seed = new Random().nextLong();
	}

	public double getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(final double cellWidth) {
		this.cellWidth = cellWidth;
	}

	public double getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(final double cellHeight) {
		this.cellHeight = cellHeight;
	}

	public double getMin() {
		return min;
	}

	public void setMin(final double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(final double max) {
		this.max = max;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(final long seed) {
		this.seed = seed;
	}
}