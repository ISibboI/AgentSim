package de.isibboi.agentsim.ui.component;

import java.awt.Graphics2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.Renderer;
import de.isibboi.agentsim.ui.event.MouseButton;
import de.isibboi.agentsim.ui.event.UIActionListener;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;
import de.isibboi.agentsim.ui.event.UserActionEvent;

/**
 * A button.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class UIButton extends UILabel implements UIMouseInputListener {
	private static final Logger LOG = LogManager.getLogger(UIButton.class);

	private boolean _mouseHovering = false;
	private boolean _mouseDown = false;
	private UIActionListener _listener;

	/**
	 * Creates a new button with an empty text.
	 * 
	 * @param renderer the renderer used to draw the component.
	 * @param position the position of the button.
	 * @param width the width of the button.
	 * @param text the label of the button.
	 */
	public UIButton(final Renderer renderer, final Point position, final int width, final String text) {
		this(renderer, position, width, text, null);
	}

	/**
	 * Creates a new button with an empty text.
	 * 
	 * @param renderer the renderer used to draw the component.
	 * @param position the position of the button.
	 * @param width the width of the button.
	 * @param text the label of the button.
	 * @param listener the action listener.
	 */
	public UIButton(final Renderer renderer, final Point position, final int width, final String text, final UIActionListener listener) {
		super(renderer, position, width, text);
		setActionListener(listener);
	}

	/**
	 * Sets the {@link UIActionListener}.
	 * 
	 * @param listener the action listener.
	 */
	public void setActionListener(final UIActionListener listener) {
		_listener = listener;
	}

	@Override
	public void draw(final Graphics2D g, final double transition) {
		if (_mouseHovering) {
			if (_mouseDown) {
				getRenderer().drawClickedBox(getPosition().getX(), getPosition().getY(), getWidth(), getRenderer().getTextHeight() + 10);
			} else {
				getRenderer().drawHoveredBox(getPosition().getX(), getPosition().getY(), getWidth(), getRenderer().getTextHeight() + 10);
			}
		} else {
			getRenderer().drawBox(getPosition().getX(), getPosition().getY(), getWidth(), getRenderer().getTextHeight() + 10);
		}

		getRenderer().drawButtonText(getText(), getPosition().getX() + 5, getPosition().getY() + 5 + getRenderer().getTextHeight());
	}

	@Override
	public void mouseMoved(final Point oldPosition, final Point newPosition) {
		_mouseHovering = contains(newPosition);

		LOG.debug("Mouse moved: Mouse hovering: " + _mouseHovering);
	}

	@Override
	public void mouseClicked(final Point position, final MouseButton button, final boolean buttonDown) {
		if (_listener != null && contains(position) && !buttonDown) {
			_listener.userAction(new UserActionEvent(this));
		}

		_mouseDown = buttonDown;

		LOG.debug("Mouse clicked: Mouse down: " + _mouseDown);
	}
}