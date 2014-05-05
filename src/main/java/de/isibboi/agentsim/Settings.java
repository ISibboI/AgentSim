package de.isibboi.agentsim;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages the applications settings.
 * 
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class Settings {
	public static final String UI_WIDTH = "ui.width";
	public static final String UI_HEIGHT = "ui.height";
	public static final String UI_FONT_FAMILY = "ui.fontFamily";
	public static final String UI_TARGET_FRAMERATE = "ui.targetFramerate";
	
	public static final String GAME_INITIAL_GOBLIN_COUNT = "game.initialGoblinCount";
	public static final String GAME_SPAWN_RADIUS = "game.spawnRadius";
	public static final String GAME_SCALE = "game.scale";
	
	public static final String AI_LIFE_TIME = "ai.lifeTime";

	private final Logger _log = LogManager.getLogger(getClass());

	private final Properties _defaults;
	private final Properties _properties;
	private boolean _isClosed;

	/**
	 * Reads settings from the given resource path.
	 * @param resource The resource path.
	 */
	public Settings(final String resource) {
		_defaults = new Properties();
		_properties = new Properties(_defaults);

		try {
			InputStream in = getClass().getResourceAsStream(resource);

			if (in != null) {
				_properties.load(in);
			} else {
				_log.warn("Settings file not found: " + resource);
			}
		} catch (IOException e) {
			_log.error("Error reading settings!", e);
		}

		createDefaults();
	}

	/**
	 * Fills the default settings.
	 */
	private void createDefaults() {
		_defaults.setProperty(UI_WIDTH, "1920");
		_defaults.setProperty(UI_HEIGHT, "1080");
		_defaults.setProperty(UI_FONT_FAMILY, "Monospaced");
		_defaults.setProperty(UI_TARGET_FRAMERATE, "60");
		
		_defaults.setProperty(GAME_INITIAL_GOBLIN_COUNT, "10");
		_defaults.setProperty(GAME_SPAWN_RADIUS, "10");
		_defaults.setProperty(GAME_SCALE, "4");
		
		_defaults.setProperty(AI_LIFE_TIME, "500");
	}

	/**
	 * Sets a setting to the given value.
	 * @param key The name of the setting.
	 * @param value The new value of the setting.
	 */
	public void set(final String key, final String value) {
		if (_isClosed) {
			throw new IllegalStateException("Settings are already closed!");
		}

		String oldValue = (String) _properties.setProperty(key, value);

		_log.info("Changed setting " + key + " from " + oldValue + " to " + value);
	}

	/**
	 * Sets a setting to the given long.
	 * @param key The name of the setting.
	 * @param value The new value of the setting.
	 */
	public void set(final String key, final long value) {
		set(key, Long.toString(value));
	}

	/**
	 * Returns the value of a setting.
	 * @param key The name of the setting.
	 * @return The value of the setting.
	 */
	public String get(final String key) {
		String result = _properties.getProperty(key);

		if (result == null) {
			throw new IllegalArgumentException("Setting " + key + " is not set!");
		}

		return result;
	}

	/**
	 * Returns the value of a given setting as integer.
	 * @param key The name of the setting.
	 * @return The value of the setting.
	 */
	public int getInt(final String key) {
		String value = get(key);

		try {
			int result = Integer.parseInt(value);
			return result;
		} catch (NumberFormatException e) {
			_log.error("Setting is not an int: (" + key + ": " + value + ")", e);
			throw new IllegalArgumentException(e);
		}
	}
}