package de.isibboi.agentsim;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.map.GenerationParameters;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.MaterialFactory;
import de.isibboi.agentsim.game.map.GenerationParameters.GenerationType;

public final class Environment {
	private static final Logger LOG = LogManager.getLogger(Environment.class);

	public static final String VERSION;
	
	public static final MaterialFactory MATERIAL_FACTORY;

	static {
		// Load values from .properties file
		Properties properties = new Properties();

		try {
			properties.load(Environment.class.getResourceAsStream("/.properties"));
		} catch (IOException e) {
			LOG.fatal("Could not load properties!", e);
		}

		VERSION = properties.getProperty("version");
		
		// Create materials
		MATERIAL_FACTORY = new MaterialFactory();
		MATERIAL_FACTORY.addMaterial("Air", 0xffffff, false, new GenerationParameters(GenerationType.DENSITY_MAP, -1e100, 255));
		MATERIAL_FACTORY.addMaterial("Dirt", 0xaa5555, true, new GenerationParameters(GenerationType.DENSITY_MAP, 255, 270));
		MATERIAL_FACTORY.addMaterial("Stone", 0x555555, true, new GenerationParameters(GenerationType.DENSITY_MAP, 270, 1e100));
	}
}
