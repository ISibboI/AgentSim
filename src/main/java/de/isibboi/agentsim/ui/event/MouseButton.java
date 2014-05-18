package de.isibboi.agentsim.ui.event;

import java.awt.event.MouseEvent;

/**
 * The buttons of the mouse.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public enum MouseButton {
	LEFT, MIDDLE, RIGHT;

	/**
	 * Translates the given AWT mouse button code to a {@link MouseButton}.
	 * 
	 * @param button the AWT mouse button code.
	 * @return the {@code MouseButton}
	 */
	public static MouseButton translateAWTMouseButton(final int button) {
		switch (button) {
		case MouseEvent.BUTTON1:
			return LEFT;
		case MouseEvent.BUTTON2:
			return MIDDLE;
		case MouseEvent.BUTTON3:
			return RIGHT;
		default:
			return null;
		}
	}
}
