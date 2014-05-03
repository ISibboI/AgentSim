package de.isibboi.agentsim;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Settings {
	public static final String UI_WIDTH = "ui.width";
	public static final String UI_HEIGHT = "ui.height";

	private final Logger log = LogManager.getLogger(getClass());

	private final Properties defaults;
	private final Properties properties;
	private boolean _isClosed;

	public Settings(String resource) {
		defaults = new Properties();
		properties = new Properties(defaults);

		try {
			InputStream in = getClass().getResourceAsStream(resource);

			if (in != null) {
				properties.load(in);
			} else {
				log.warn("Settings file not found: " + resource);
			}
		} catch (IOException e) {
			log.error("Error reading settings!", e);
		}

		createDefaults();
	}

	private void createDefaults() {
		defaults.setProperty("ui.width", "1920");
		defaults.setProperty("ui.height", "1080");
	}

	public void set(String key, String value) {
		if (_isClosed) {
			throw new IllegalStateException("Settings are already closed!");
		}

		String oldValue = (String) properties.setProperty(key, value);

		log.info("Changed setting " + key + " from " + oldValue + " to " + value);
	}

	public void set(String key, long value) {
		set(key, Long.toString(value));
	}

	public String get(String key) {
		String result = properties.getProperty(key);

		if (result == null) {
			throw new IllegalArgumentException("Setting " + key + " is not set!");
		}

		return result;
	}

	public int getInt(String key) {
		String value = get(key);

		try {
			int result = Integer.parseInt(value);
			return result;
		} catch (NumberFormatException e) {
			log.error("Setting is not an int: (" + key + ": " + value + ")", e);
			throw new IllegalArgumentException(e);
		}
	}
}