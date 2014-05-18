package de.isibboi.agentsim.ui.component;

import java.awt.Graphics2D;

import de.isibboi.agentsim.game.map.Point;
import de.isibboi.agentsim.ui.Renderer;
/**
 * A ui component to display text.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class UILabel extends UIAbstractComponent {
	private String _text;
	
	/**
	 * Creates a new ui label with an empty text.
	 * 
	 * @param renderer the renderer used to draw the component.
	 * @param position the position of the label.
	 * @param width the width of the label.
	 */
	public UILabel(final Renderer renderer, final Point position, final int width) {
		this(renderer, position, width, "");
	}
	
	/**
	 * Creates a new ui label with the given text.
	 * 
	 * @param renderer the renderer used to draw the component.
	 * @param position the position of the label.
	 * @param width the width of the label.
	 * @param text The text of the label.
	 */
	public UILabel(final Renderer renderer, final Point position, final int width, final String text) {
		super(renderer, position, width, renderer.getTextHeight() + 10);
		setText(text);
	}

	@Override
	public void draw(final Graphics2D g) {
		getRenderer().drawBox(getPosition().getX(), getPosition().getY(), getWidth() + 10, getRenderer().getTextHeight() + 10);
		getRenderer().drawText(_text, getPosition().getX() + 5, getPosition().getY() + 5 + getRenderer().getTextHeight());
	}
	
	/**
	 * Sets the text of this label.
	 * 
	 * @param text the new text.
	 */
	public void setText(final String text) {
		_text = text;
	}

	/**
	 * @return the text.
	 */
	public String getText() {
		return _text;
	}
}