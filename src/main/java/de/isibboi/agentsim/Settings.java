package de.isibboi.agentsim;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.ui.EntitySelectionManager;

/**
 * Manages the applications settings.
 * 
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public class Settings {
	public static final String UI_WIDTH = "ui.width";
	public static final String UI_HEIGHT = "ui.height";
	public static final String UI_X_POS = "ui.xPos";
	public static final String UI_Y_POS = "ui.yPos";
	public static final String UI_FONT_FAMILY = "ui.fontFamily";
	public static final String UI_SELECTION_MODE = "ui.selectionMode";

	public static final String GAME_INITIAL_GOBLIN_COUNT = "game.initialGoblinCount";
	public static final String GAME_SPAWN_RADIUS = "game.spawnRadius";
	public static final String GAME_SCALE = "game.scale";

	public static final String GAME_ENTITIES_GOBLIN_INITIAL_SATURATION = "game.entities.goblin.initialSaturation";
	public static final String GAME_ENTITIES_GOBLIN_INITIAL_MAX_SATURATION = "game.entities.goblin.initialMaxSaturation";

	public static final String GAME_AI_SATURATION_BUFFER_DISTANCE_FACTOR = "game.ai.saturationBufferDistanceFactor";
	public static final String GAME_AI_SATURATION_BUFFER_MINIMUM = "game.ai.saturationBufferMinimum";

	public static final String CORE_TARGET_FRAME_RATE = "core.targetFrameRate";
	public static final String CORE_TARGET_UPDATE_RATE = "core.targetUpdateRate";
	public static final String CORE_MAX_UPDATES_PER_FRAME = "core.maxUpdatesPerFrame";

	public static final String CORE_RENDER_TRANSITION_AMOUNT = "core.render.transitionAmount";

	public static final String CORE_UPDATE_COLLISION_THREAD_COUNT = "core.update.collisionThreadCount";

	public static final String CORE_AI_KNOWLEDGE_REPRESENTATION = "core.ai.knowledgeRepresentation";
	public static final String CORE_AI_KNOWLEDGE_REPRESENTATION_ARRAY = "core.ai.knowledgeRepresentation.ARRAY";
	public static final String CORE_AI_KNOWLEDGE_REPRESENTATION_HASHMAP = "core.ai.knowledgeRepresentation.HASHMAP";

	private static final Set<String> ALL_SETTINGS = new HashSet<>(Arrays.asList(
			UI_WIDTH, UI_HEIGHT, UI_X_POS, UI_Y_POS, UI_FONT_FAMILY, UI_SELECTION_MODE,
			GAME_INITIAL_GOBLIN_COUNT, GAME_SPAWN_RADIUS, GAME_SCALE,
			GAME_ENTITIES_GOBLIN_INITIAL_SATURATION, GAME_ENTITIES_GOBLIN_INITIAL_MAX_SATURATION,
			GAME_AI_SATURATION_BUFFER_DISTANCE_FACTOR, GAME_AI_SATURATION_BUFFER_MINIMUM,
			CORE_TARGET_FRAME_RATE, CORE_TARGET_UPDATE_RATE, CORE_MAX_UPDATES_PER_FRAME,
			CORE_RENDER_TRANSITION_AMOUNT,
			CORE_UPDATE_COLLISION_THREAD_COUNT,
			CORE_AI_KNOWLEDGE_REPRESENTATION));

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
				_log.info("Settings loaded");

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
		_defaults.setProperty(UI_X_POS, "0");
		_defaults.setProperty(UI_Y_POS, "0");
		_defaults.setProperty(UI_FONT_FAMILY, Font.MONOSPACED);
		_defaults.setProperty(UI_SELECTION_MODE, EntitySelectionManager.SelectionMode.TOUCH.toString());

		_defaults.setProperty(GAME_INITIAL_GOBLIN_COUNT, "100");
		_defaults.setProperty(GAME_SPAWN_RADIUS, "10");
		_defaults.setProperty(GAME_SCALE, "4");

		_defaults.setProperty(GAME_ENTITIES_GOBLIN_INITIAL_SATURATION, "20");
		_defaults.setProperty(GAME_ENTITIES_GOBLIN_INITIAL_MAX_SATURATION, "100");

		_defaults.setProperty(GAME_AI_SATURATION_BUFFER_DISTANCE_FACTOR, "1.05");
		_defaults.setProperty(GAME_AI_SATURATION_BUFFER_MINIMUM, "5");

		_defaults.setProperty(CORE_TARGET_FRAME_RATE, "60");
		_defaults.setProperty(CORE_TARGET_UPDATE_RATE, "6");
		_defaults.setProperty(CORE_MAX_UPDATES_PER_FRAME, "2");

		_defaults.setProperty(CORE_RENDER_TRANSITION_AMOUNT, "1");

		_defaults.setProperty(CORE_UPDATE_COLLISION_THREAD_COUNT, "2");

		_defaults.setProperty(CORE_AI_KNOWLEDGE_REPRESENTATION, CORE_AI_KNOWLEDGE_REPRESENTATION_HASHMAP);
	}

	/**
	 * Finds the best settings for the CPU the program is executed on.
	 */
	// Should be placed somewhere else later on.
	public void findBestSettingsForCPU() {
		set(CORE_UPDATE_COLLISION_THREAD_COUNT, Runtime.getRuntime().availableProcessors());
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

		if (value == null || value.trim().equals("")) {
			String oldValue = (String) _properties.remove(key);
			_log.info("Removed setting " + key + ". Old value: " + oldValue);
		} else {
			String oldValue = (String) _properties.setProperty(key, value);
			_log.info("Changed setting " + key + " from " + oldValue + " to " + value);
		}
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
	 * Sets a setting to the given double.
	 * @param key The name of the setting.
	 * @param value The new value of the setting.
	 */
	public void set(final String key, final double value) {
		set(key, Double.toString(value));
	}

	/**
	 * Sets a setting to the given enum constant.
	 * @param <T> The type of the enum.
	 * @param key The name of the setting.
	 * @param value The new value of the setting.
	 */
	public <T extends Enum<T>> void set(final String key, final Enum<T> value) {
		set(key, value.toString());
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
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			_log.error("Setting is not an int: (" + key + ": " + value + ")", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns the value of a given setting as float.
	 * @param key The name of the setting.
	 * @return The value of the setting.
	 */
	public float getFloat(final String key) {
		String value = get(key);

		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			_log.error("Setting is not a float: (" + key + ": " + value + ")", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns the value of a given setting as double.
	 * @param key The name of the setting.
	 * @return The value of the setting.
	 */
	public double getDouble(final String key) {
		String value = get(key);

		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			_log.error("Setting is not a double: (" + key + ": " + value + ")", e);
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Returns the value of a given setting as enum constant.
	 * @param <T> The type of the enum.
	 * @param key The name of the setting.
	 * @param enumeration The type of the enum as class.
	 * @return The value of the setting.
	 */
	public <T extends Enum<T>> T getEnumConstant(final String key, final Class<T> enumeration) {
		return Enum.valueOf(enumeration, get(key));
	}

	/**
	 * Saves the current settings to disk and prevents any further modification.
	 */
	public void close() {
		_isClosed = true;

		File f = new File(_settingsFile);

		try (FileOutputStream out = new FileOutputStream(f)) {
			_properties.store(out, "Created by Agent Sim version " + Environment.VERSION);
			_log.info("Settings saved");
		} catch (IOException e) {
			_log.error("Could not save settings: " + _settingsFile);
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} stating that the value of the given setting is invalid.
	 * 
	 * @param key The setting that has an invalid value.
	 */
	public void throwIllegalSettingValue(final String key) {
		throw new IllegalArgumentException("Setting '" + key + "' has an illegal value: " + get(key));
	}
}
