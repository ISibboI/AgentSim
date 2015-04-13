package de.isibboi.agentsim.algorithm.noise;

/**
 * A function that scales a decorated noise function.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class ScaledNoise implements Noise {
	private final Noise _decoratee;
	
	private final double _cellWidth;
	private final double _cellHeight;
	private final double _min;
	private final double _max;

	/**
	 * Creates a new scaled simplex noise.
	 * Note that the cell width and height do not influence the shape of the simplex noise cells, but scale the input parameters.
	 * 
	 * @param decoratee The decoratee.
	 * @param cellWidth The width of one simplex cell.
	 * @param cellHeight The height of one simplex cell.
	 * @param min The desired minimum of the noise results.
	 * @param max The desired maximum of the noise results.
	 */
	public ScaledNoise(final Noise decoratee, final double cellWidth, final double cellHeight, final double min, final double max) {
		_decoratee = decoratee;
		_cellWidth = cellWidth;
		_cellHeight = cellHeight;
		_min = min;
		_max = max;
	}

	/**
	 * Creates a new scaled noise.
	 * 
	 * @param decoratee The decoratee.
	 * @param noiseParameter The parameters of the noise.
	 */
	public ScaledNoise(final Noise decoratee, final NoiseParameter noiseParameter) {
		_decoratee = decoratee;
		_cellWidth = noiseParameter.getCellWidth();
		_cellHeight = noiseParameter.getCellHeight();
		_min = noiseParameter.getMin();
		_max = noiseParameter.getMax();
	}

	@Override
	public double noise(final double x, final double y) {
		double result = _decoratee.noise(x / _cellWidth, y / _cellHeight); 

		double halfTargetIntervalSize = (_max - _min) / 2;

		result *= halfTargetIntervalSize;
		result += halfTargetIntervalSize + _min;

		return result;
	}
}