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
	 * @param g The graphics object used for drawing.
	 */
	void drawScaledContent(Graphics2D g);

	/**
	 * Draws the content that doesn't need to be scaled.
	 * @param g The graphics object used for drawing.
	 */
	void drawUnscaledContent(Graphics2D g);
}