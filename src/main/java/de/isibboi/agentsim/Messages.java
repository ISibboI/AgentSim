package de.isibboi.agentsim;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Auto-generated string externalization class.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public final class Messages {
	private static final Logger LOG = LogManager.getLogger(Messages.class);
	
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static final String UI_SETTINGS_FRAME_RESTART_LABEL = "UISettingsFrame.restartLabel";
	public static final String UI_SETTINGS_FRAME_UI_SIZE_LABEL = "UISettingsFrame.uiSizeLabel";
	public static final String UI_SETTINGS_FRAME_UI_TAB = "UISettingsFrame.uiTab";
	public static final String UI_SETTINGS_FRAME_CORE_FRAME_RATE_LABEL = "UISettingsFrame.coreFrameRateLabel";
	public static final String UI_SETTINGS_FRAME_CORE_UPDATE_RATE_LABEL = "UISettingsFrame.coreUpdateRateLabel";
	public static final String UI_SETTINGS_FRAME_CORE_TAB = "UISettingsFrame.coreTab";

	/**
	 * Utility class.
	 */
	private Messages() {
	}

	/**
	 * Returns the string associated with the given key. The string is returned in the system default language, if possible.
	 * 
	 * @param key the key.
	 * @return the string associated with the given key.
	 */
	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			LOG.warn("Could not find string for: " + key, e);
			return '!' + key + '!';
		}
	}
}
