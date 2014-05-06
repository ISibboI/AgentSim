package de.isibboi.agentsim;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

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

	private static final Set<String> ALL_SETTINGS = new HashSet<>(Arrays.asList(
			UI_WIDTH, UI_HEIGHT, UI_FONT_FAMILY, UI_TARGET_FRAMERATE, GAME_INITIAL_GOBLIN_COUNT, GAME_SPAWN_RADIUS, GAME_SCALE, AI_LIFE_TIME));

	private final Logger _log = LogManager.getLogger(getClass());

	private final String _settingsFile;
	private final Properties _defaults;
	private final Properties _properties;
	private boolean _isClosed;

	/**
	 * Reads settings from the given resource path.
	 * @param settingsFile The resource path.
	 */
	public Settings(final String settingsFile) {
		_defaults = new Properties();
		_properties = new Properties(_defaults);
		_settingsFile = settingsFile;

		File f = new File(settingsFile);

		if (f.isFile()) {
			try (InputStream in = new FileInputStream(f)) {
				_properties.load(in);

				for (String property : _properties.stringPropertyNames()) {
					if (!ALL_SETTINGS.contains(property)) {
						_log.warn("Unknown setting: " + property);
					}
				}
			} catch (IOException e) {
				_log.error("Error reading settings!", e);
			}
		} else {
			_log.warn("Settings file not found: " + settingsFile);
		}

		createDefaults();
	}

	/**
	 * Fills the default settings.
	 */
	private void createDefaults() {
		_defaults.setProperty(UI_WIDTH, "800");
		_defaults.setProperty(UI_HEIGHT, "600");
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

	/**
	 * Saves the current settings to disk and prevents any further modification.
	 */
	public void close() {
		_isClosed = true;

		File f = new File(_settingsFile);
		
		try (FileOutputStream out = new FileOutputStream(f)) {
			_properties.store(out, "Created by Agent Sim version " + Environment.VERSION);
		} catch (IOException e) {
			_log.error("Could not save settings: " + _settingsFile);
		}
	}
}
