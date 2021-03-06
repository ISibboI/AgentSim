package de.isibboi.agentsim.algorithm.noise;

import java.util.Random;

/**
 * The parameters for a {@link ScaledNoise}.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class NoiseParameter {
	private double _cellWidth;
	private double _cellHeight;
	private double _min;
	private double _max;
	private long _seed;

	/**
	 * Creates new noise parameters. The seed is randomly generated by this function.
	 * @param cellWidth The simplex cell width.
	 * @param cellHeight The simplex cell height.
	 * @param min The desired global minimum of the noise function.
	 * @param max The desired global maximum of the noise function.
	 */
	public NoiseParameter(final double cellWidth, final double cellHeight, final double min, final double max) {
		this._cellWidth = cellWidth;
		this._cellHeight = cellHeight;
		this._min = min;
		this._max = max;
		this._seed = new Random().nextLong();
	}

	/**
	 * Returns the cell width.
	 * @return The cell width.
	 */
	public double getCellWidth() {
		return _cellWidth;
	}

	/**
	 * Sets the cell width to the given value.
	 * @param cellWidth The new cell width.
	 */
	public void setCellWidth(final double cellWidth) {
		this._cellWidth = cellWidth;
	}

	/**
	 * Returns the cell height.
	 * @return The cell height.
	 */
	public double getCellHeight() {
		return _cellHeight;
	}

	/**
	 * Sets the cell height to the given value.
	 * @param cellHeight The new cell height.
	 */
	public void setCellHeight(final double cellHeight) {
		this._cellHeight = cellHeight;
	}

	/**
	 * Returns the global minimum.
	 * @return The global minimum.
	 */
	public double getMin() {
		return _min;
	}

	/**
	 * Sets the global minimum to the given value.
	 * @param min The new global minimum.
	 */
	public void setMin(final double min) {
		this._min = min;
	}

	/**
	 * Returns the global maximum.
	 * @return The global maximum.
	 */
	public double getMax() {
		return _max;
	}

	/**
	 * Sets the global maximum to the given value.
	 * @param max The new global maximum.
	 */
	public void setMax(final double max) {
		this._max = max;
	}

	/**
	 * Returns the seed.
	 * @return The seed.
	 */
	public long getSeed() {
		return _seed;
	}

	/**
	 * Sets the seed to the given value.
	 * @param seed The new seed.
	 */
	public void setSeed(final long seed) {
		this._seed = seed;
	}
}