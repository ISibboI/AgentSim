package de.isibboi.agentsim.game.map;

/**
 * The generation parameters for a material. They are a set of parameters that decide where a block with a certain material should be generated.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class GenerationParameters {
	/**
	 * The step in the generation process the material is added.
	 */
	public enum GenerationType {
		DENSITY_MAP
	}
	
	private final GenerationType _generationType;
	private final double _min;
	private final double _max;
	
	/**
	 * Creates new single range generation parameters.
	 * @param generationType The step in the generation process the material is added.
	 * @param min The lower end of the range the material is spawned in.
	 * @param max The upper end of the range the material is spawned in.
	 */
	public GenerationParameters(final GenerationType generationType, final double min, final double max) {
		_generationType = generationType;
		_min = min;
		_max = max;
	}

	/**
	 * Return the generation type of this material.
	 * @return The generation type.
	 * @see GenerationType
	 */
	public GenerationType getGenerationType() {
		return _generationType;
	}

	/**
	 * Returns the lower limit of the generation range.
	 * @return The lower limit of the generation range.
	 */
	public double getMin() {
		return _min;
	}

	/**
	 * Returns the upper limit of the generation range.
	 * @return The upper limit of the generation range.
	 */
	public double getMax() {
		return _max;
	}

	/**
	 * Returns if the given sample is in the generation range.
	 * @param sample The sample.
	 * @return True if the sample is in the generation range.
	 */
	public boolean inRange(final double sample) {
		return sample >= _min && sample < _max;
	}
}
