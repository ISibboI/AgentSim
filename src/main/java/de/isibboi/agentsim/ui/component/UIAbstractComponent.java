package de.isibboi.agentsim.ui.component;

import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.Renderer;

/**
 * An abstract ui component that handles the renderer.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public abstract class UIAbstractComponent implements UIComponent {
	private final Renderer _renderer;
	private Point _position;
	private int _width;
	private int _height;

	/**
	 * Creates a new abstract ui component with the given renderer.
	 * 
	 * @param renderer the renderer used to render this component.
	 * @param position the position of the component.
	 * @param width the width of the component.
	 * @param height the height of the component.
	 */
	public UIAbstractComponent(final Renderer renderer, final Point position, final int width, final int height) {
		_renderer = renderer;
		_position = position;
		_width = width;
		_height = height;
	}

	/**
	 * Returns the renderer used to render this component.
	 * @return The renderer of this component.
	 */
	public Renderer getRenderer() {
		return _renderer;
	}

	@Override
	public boolean contains(final Point position) {
		return position.getX() >= _position.getX() && position.getY() >= _position.getY()
				&& position.getX() <= _position.getX() + _width && position.getY() <= _position.getY() + _height;
	}

	/**
	 * @return the position.
	 */
	public Point getPosition() {
		return _position;
	}

	/**
	 * @return the width.
	 */
	public int getWidth() {
		return _width;
	}

	/**
	 * @return the height.
	 */
	public int getHeight() {
		return _height;
	}

	@Override
	public int getDrawPriority() {
		return 1000;
	}
}