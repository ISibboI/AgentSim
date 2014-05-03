package de.isibboi.agentsim.noise;

import java.util.Random;

/**
 * A function that scales the simplex noise function.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class ScaledSimplexNoise extends SimplexNoise implements Noise {
	private double _cellWidth;
	private double _cellHeight;
	private double _min;
	private double _max;

	/**
	 * Creates a new scaled simplex noise.
	 * Note that the cell width and height do not influence the shape of the simplex noise cells, but scale the input parameters.
	 * 
	 * @param seed The seed for the noise.
	 * @param cellWidth The width of one simplex cell.
	 * @param cellHeight The height of one simplex cell.
	 * @param min The desired minimum of the noise results.
	 * @param max The desired maximum of the noise results.
	 */
	public ScaledSimplexNoise(final long seed, final double cellWidth, final double cellHeight, final double min, final double max) {
		super(seed);

		_cellWidth = cellWidth;
		_cellHeight = cellHeight;
		_min = min;
		_max = max;
	}

	/**
	 * Creates a mew scaled simplex noise.
	 * @param noiseParameter The parameters of the noise.
	 */
	public ScaledSimplexNoise(final NoiseParameter noiseParameter) {
		super(noiseParameter.getSeed());

		_cellWidth = noiseParameter.getCellWidth();
		_cellHeight = noiseParameter.getCellHeight();
		_min = noiseParameter.getMin();
		_max = noiseParameter.getMax();
	}

	/**
	 * Creates a new scaled simplex noise.
	 * Note that the cell width and height do not influence the shape of the simplex noise cells, but scale the input parameters.
	 * This constructor generates a random seed.
	 * 
	 * @param cellWidth The width of one simplex cell.
	 * @param cellHeight The height of one simplex cell.
	 * @param min The desired minimum of the noise results.
	 * @param max The desired maximum of the noise results.
	 */
	public ScaledSimplexNoise(final double cellWidth, final double cellHeight, final double min, final double max) {
		super(new Random().nextLong());

		_cellWidth = cellWidth;
		_cellHeight = cellHeight;
		_min = min;
		_max = max;
	}

	@Override
	public double noise(final double x, final double y) {
		double result = super.noise(x / _cellWidth, y / _cellHeight);

		double halfTargetIntervalSize = (_max - _min) / 2;

		result *= halfTargetIntervalSize;
		result += halfTargetIntervalSize + _min;

		return result;
	}
}