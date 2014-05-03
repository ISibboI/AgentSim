package de.isibboi.agentsim.game.map;

/**
 * A material that can be found on the map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class Material {
	private final int _color;
	private final GenerationParameters _generationParameters;

	Material(final int color, final GenerationParameters generationParameters) {
		_color = color;
		_generationParameters = generationParameters;
	}

	public int getColor() {
		return _color;
	}

	public GenerationParameters getGenerationParameters() {
		return _generationParameters;
	}

	public boolean inRange(final double sample) {
		return _generationParameters.inRange(sample);
	}
}