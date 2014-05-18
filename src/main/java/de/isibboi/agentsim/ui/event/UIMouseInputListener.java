package de.isibboi.agentsim.ui.event;

import de.isibboi.agentsim.game.map.Point;

/**
 * A listener for mouse events.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0 
 */
public interface UIMouseInputListener {
	/**
	 * The mouse was moved.
	 * 
	 * @param oldPosition the old position of the mouse.
	 * @param newPosition the new position of the mouse.
	 */
	void mouseMoved(Point oldPosition, Point newPosition);
	
	/**
	 * A mouse button was pressed or released.
	 * 
	 * @param position the position the event happened.
	 * @param button the button that was changed.
	 * @param buttonDown true if the button is now down, false if it is now up.
	 */
	void mouseClicked(Point position, MouseButton button, boolean buttonDown);
}