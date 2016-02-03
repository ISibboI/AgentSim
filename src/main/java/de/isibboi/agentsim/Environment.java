package de.isibboi.agentsim;

import java.io.IOException;
import java.util.Properties;

import de.isibboi.agentsim.game.map.GenerationParameters;
import de.isibboi.agentsim.game.map.GenerationParameters.GenerationType;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.MaterialFactory;

/**
 * Contains environment variables for the application.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public final class Environment {

	public static final String VERSION;

	public static final MaterialFactory MATERIAL_FACTORY;

	// Materials
	public static final Material MATERIAL_AIR;
	public static final Material MATERIAL_DIRT;
	public static final Material MATERIAL_STONE;

	static {
		// Load values from .properties file
		Properties properties = new Properties();

		try {
			properties.load(Environment.class.getResourceAsStream("/.properties"));
		} catch (IOException e) {
		}

		VERSION = properties.getProperty("version");

		// Create materials
		MATERIAL_FACTORY = new MaterialFactory();
		MATERIAL_AIR = MATERIAL_FACTORY.addMaterial("Air", 0xffffff, false, 0, new GenerationParameters(GenerationType.DENSITY_MAP, -1e100, 255));
		MATERIAL_DIRT = MATERIAL_FACTORY.addMaterial("Dirt", 0xaa5555, true, 10, new GenerationParameters(GenerationType.DENSITY_MAP, 255, 300));
		MATERIAL_STONE = MATERIAL_FACTORY.addMaterial("Stone", 0x555555, true, 100, new GenerationParameters(GenerationType.DENSITY_MAP, 300, 1e100));
	}

	/**
	 * Unused.
	 */
	private Environment() {
	}
}
