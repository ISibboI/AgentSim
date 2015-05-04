package de.isibboi.agentsim.game.entities;

import java.awt.Graphics2D;

/**
 * Classes implementing this can be drawn on the screen.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Drawable {
	/**
	 * Draw the object on the screen.
	 * The transition parameter determines where the entity is exactly drawn.
	 * For zero, the entity should be drawn at the location from the last update, for one, it should be drawn at the current location.
	 * Values between zero and one should be used for linear interpolation between old and new location.
	 * 
	 * @param g The screen graphics.
	 * @param transition A value between zero and one.
	 */
	void draw(Graphics2D g, double transition);
}
