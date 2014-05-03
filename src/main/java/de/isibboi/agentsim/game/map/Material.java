package de.isibboi.agentsim.game.map;

/**
 * A material that can be found on the map.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class Material {
	private final String _name;
	private final int _color;

	private final boolean _solid;
	private final GenerationParameters _generationParameters;

	Material(final String name, final int color, final boolean solid, final GenerationParameters generationParameters) {
		_name = name;
		_color = color;
		_solid = solid;
		_generationParameters = generationParameters;
	}

	public String getName() {
		return _name;
	}

	public int getColor() {
		return _color;
	}

	public boolean isSolid() {
		return _solid;
	}

	public GenerationParameters getGenerationParameters() {
		return _generationParameters;
	}

	public boolean inRange(final double sample) {
		return _generationParameters.inRange(sample);
	}
}