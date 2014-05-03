package de.isibboi.agentsim;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Environment {
	private static final Logger LOG = LogManager.getLogger(Environment.class);

	public static final String VERSION;

	// Load values from .properties file
	static {
		Properties properties = new Properties();

		try {
			properties.load(Environment.class.getResourceAsStream("/.properties"));
		} catch (IOException e) {
			LOG.fatal("Could not load properties!", e);
		}

		VERSION = properties.getProperty("version");
	}
}
