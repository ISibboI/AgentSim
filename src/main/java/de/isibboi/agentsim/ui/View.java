package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;

import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.ui.event.UIActionListener;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;

/**
 * A certain view on the game. Every view defines a UI. The views can be exchanged to show different UI elements, like a main menu.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface View extends UIMouseInputListener, UIActionListener, Updateable {
	/**
	 * Activate the {@link View}.
	 */
	void activate();

	/**
	 * Deactivate the {@link View}.
	 */
	void deactivate();

	/**
	 * Draws the content that needs to be scaled when the game is for example zoomed.
	 * The transition parameter determines where the entity is exactly drawn.
	 * For zero, the entity should be drawn at the location from the last update, for one, it should be drawn at the current location.
	 * Values between zero and one should be used for linear interpolation between old and new location.
	 * 
	 * @param g The graphics object used for drawing.
	 * @param transition A value between zero and one.
	 */
	void drawScaledContent(Graphics2D g, double transition);

	/**
	 * Draws the content that doesn't need to be scaled.
	 * The transition parameter determines where the entity is exactly drawn.
	 * For zero, the entity should be drawn at the location from the last update, for one, it should be drawn at the current location.
	 * Values between zero and one should be used for linear interpolation between old and new location.
	 * 
	 * @param g The graphics object used for drawing.
	 * @param transition A value between zero and one.
	 */
	void drawUnscaledContent(Graphics2D g, double transition);
}