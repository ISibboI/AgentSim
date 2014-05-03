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
	private final int _durability;

	private final GenerationParameters _generationParameters;

	/**
	 * Creates a new Material.
	 * @param name The name.
	 * @param color The color.
	 * @param solid True if the material should block entities.
	 * @param durability The durability. The higher the longer it takes to mine the material.
	 * @param generationParameters The parameters defining where the material should be generated.
	 */
	Material(final String name, final int color, final boolean solid, final int durability, final GenerationParameters generationParameters) {
		_name = name;
		_color = color;
		_solid = solid;
		_durability = durability;
		_generationParameters = generationParameters;
	}

	/**
	 * Returns the name of the material.
	 * @return The name of the material.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Returns the color of the material.
	 * @return The color of the material.
	 */
	public int getColor() {
		return _color;
	}

	/**
	 * Returns if the material is solid.
	 * @return True if the material is solid.
	 */
	public boolean isSolid() {
		return _solid;
	}

	/**
	 * Returns the durability of the material.
	 * @return The durability of the material.
	 */
	public int getDurability() {
		return _durability;
	}

	/**
	 * Returns the name of the material.
	 * @return The name of the material.
	 */
	public GenerationParameters getGenerationParameters() {
		return _generationParameters;
	}

	/**
	 * Returns if the given sample is in this materials generation range.
	 * @param sample The sample.
	 * @return True if the sample is in the generation range.
	 */
	public boolean inRange(final double sample) {
		return _generationParameters.inRange(sample);
	}
}