package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;

import de.isibboi.agentsim.game.map.Point;
/**
 * A ui component to display text.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class UILabel extends UIAbstractComponent {
	private String _text;
	private Point _position;
	private int _width;
	
	/**
	 * Creates a new ui label with an empty text.
	 * 
	 * @param renderer The renderer used to draw the component.
	 * @param bounds The bounds of the label.
	 * @param width The width of the label.
	 */
	public UILabel(final Renderer renderer, final Point position, final int width) {
		super(renderer);
		
		_position = position;
		_width = width;
	}
	
	/**
	 * Sets the text of this label.
	 * @param text The new text.
	 */
	public void setText(final String text) {
		_text = text;
	}

	@Override
	public void draw(final Graphics2D g) {
		getRenderer().drawBox(_position.getX(), _position.getY(), _width + 10, getRenderer().getTextHeight() + 10);
		getRenderer().drawText(_text, _position.getX() + 5, _position.getY() + 5 + getRenderer().getTextHeight());
	}
}