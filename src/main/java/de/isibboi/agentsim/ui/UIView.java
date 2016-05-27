package de.isibboi.agentsim.ui;

import java.util.ArrayList;
import java.util.Collection;

import de.isibboi.agentsim.Settings;
import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.event.MouseButton;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;

/**
 * An abstract UI that handles mouse events and drawing of static content like UI components.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 */
public abstract class UIView implements View {
	private final Renderer _renderer;

	private final Collection<Drawable> _drawables = new ArrayList<>();
	private final Collection<UIMouseInputListener> _mouseListeners = new ArrayList<>();

	private int _width;
	private int _height;

	/**
	 * Creates a new UIView with the given renderer.
	 * @param renderer The renderer.
	 * @param settings The settings.
	 */
	public UIView(final Renderer renderer, final Settings settings) {
		_renderer = renderer;

		_width = settings.getInt(Settings.UI_WIDTH);
		_height = settings.getInt(Settings.UI_HEIGHT);
	}

	@Override
	public void mouseMoved(final Point oldPosition, final Point newPosition) {
		for (UIMouseInputListener listener : _mouseListeners) {
			listener.mouseMoved(oldPosition, newPosition);
		}
	}

	@Override
	public void mouseClicked(final Point position, final MouseButton button, final boolean buttonDown) {
		for (UIMouseInputListener listener : _mouseListeners) {
			listener.mouseClicked(position, button, buttonDown);
		}
	}

	/**
	 * Returns the renderer used to render this view.
	 * @return The renderer.
	 */
	protected Renderer getRenderer() {
		return _renderer;
	}

	/**
	 * Adds the given {@code Drawable} to the view.
	 * @param drawable The {@code Drawable}.
	 */
	protected void add(final Drawable drawable) {
		_drawables.add(drawable);
	}

	/**
	 * Adds the given {@code UIMouseInputListener}.
	 * @param listener The listener.
	 */
	protected void addMouseListener(final UIMouseInputListener listener) {
		_mouseListeners.add(listener);
	}

	/**
	 * Returns the width of the view.
	 * @return The width of the view.
	 */
	protected int getWidth() {
		return _width;
	}

	/**
	 * Returns the height of the view.
	 * @return The height of the view.
	 */
	protected int getHeight() {
		return _height;
	}
}