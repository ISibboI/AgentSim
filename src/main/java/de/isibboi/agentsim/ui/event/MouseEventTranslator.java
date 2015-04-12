package de.isibboi.agentsim.ui.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.map.Point;

/**
 * Translates AWT mouse events.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class MouseEventTranslator implements MouseListener, MouseMotionListener {
	private final Logger _log = LogManager.getLogger(getClass());

	private final List<UIMouseInputListener> _listeners = new ArrayList<>();

	private Point _lastMousePosition;

	/**
	 * Creates a new {@link MouseEventTranslator} with no listeners.
	 */
	public MouseEventTranslator() {
	}

	/**
	 * Creates a new {@link MouseEventTranslator} with the given listener.
	 * 
	 * @param listener the listener.
	 */
	public MouseEventTranslator(final UIMouseInputListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Adds the given listener.
	 * 
	 * @param listener the listener.
	 */
	public void addUIMouseInputListener(final UIMouseInputListener listener) {
		_listeners.add(listener);
	}

	/**
	 * Removes the given listener.
	 * 
	 * @param listener the listener.
	 */
	public void removeUIMouseInputListener(final UIMouseInputListener listener) {
		_listeners.remove(listener);
	}

	/**
	 * Fires a mouse moved event.
	 * 
	 * @param oldPosition the old mouse position.
	 * @param newPosition the new mouse position.
	 */
	private void fireMouseMoved(final Point oldPosition, final Point newPosition) {
		for (UIMouseInputListener listener : _listeners) {
			listener.mouseMoved(oldPosition, newPosition);
		}

		_log.debug("Fired mouse moved: Old position: " + oldPosition + " New position: " + newPosition);
	}

	/**
	 * Fires a mouse clicked event.
	 * 
	 * @param position the mouse position.
	 * @param button the button that was changed.
	 * @param buttonDown true if the button is down now, false if it is up now.
	 */
	private void fireMouseClicked(final Point position, final MouseButton button, final boolean buttonDown) {
		for (UIMouseInputListener listener : _listeners) {
			listener.mouseClicked(position, button, buttonDown);
		}

		_log.debug("Fired mouse clicked: Position: " + position + " Button: " + button + " Button down: " + buttonDown);
	}

	/**
	 * Ensures that all listeners are aware that the mouse is currently at the given position.
	 * 
	 * @param position the position of the mouse.
	 */
	private void ensureMousePosition(final Point position) {
		if (_lastMousePosition != null) {
			if (!_lastMousePosition.equals(position)) {
				fireMouseMoved(_lastMousePosition, position);
				_lastMousePosition = position;
			}
		} else {
			_lastMousePosition = position;
		}
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		Point position = translatePoint(e.getX(), e.getY());
		ensureMousePosition(position);
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		Point position = translatePoint(e.getX(), e.getY());
		ensureMousePosition(position);
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		Point position = translatePoint(e.getX(), e.getY());
		MouseButton mouseButton = MouseButton.translateAWTMouseButton(e.getButton());
		ensureMousePosition(position);

		if (mouseButton != null) {
			fireMouseClicked(position, mouseButton, true);
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		Point position = translatePoint(e.getX(), e.getY());
		MouseButton mouseButton = MouseButton.translateAWTMouseButton(e.getButton());
		ensureMousePosition(position);

		if (mouseButton != null) {
			fireMouseClicked(position, mouseButton, false);
		}
	}

	/**
	 * Translates the mouse position received by the AWT event system to the coordinate system used for the UI. 
	 * @param x The x coordinate received from the AWT event system.
	 * @param y The y coordinate received from the AWT event system.
	 * @return The point in the UI coordinate system.
	 */
	private Point translatePoint(final int x, final int y) {
		return new Point(x, y);
	}
}