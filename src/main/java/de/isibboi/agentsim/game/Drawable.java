package de.isibboi.agentsim.game;

import java.awt.Graphics2D;

/**
 * Classes implementing this can be drawn on the screen.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Drawable {
	/**
	 * Draw the object on the screen.
	 * @param g The screen graphics.
	 */
	void draw(Graphics2D g);
}
