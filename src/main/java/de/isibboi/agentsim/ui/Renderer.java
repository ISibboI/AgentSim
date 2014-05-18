package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;

/**
 * Provides functions to render basic ui elements.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Renderer {
	/**
	 * Sets the graphics object to draw on.
	 * 
	 * @param g the graphics object to draw on.
	 */
	void setGraphics(Graphics2D g);

	/**
	 * Draws a box.
	 * 
	 * @param x The x coordinate of the upper left corner of the box.
	 * @param y The y coordinate of the upper left corner of the box.
	 * @param width the width of the box.
	 * @param height the height of the box.
	 */
	void drawBox(int x, int y, int width, int height);

	/**
	 * Draws a box. The mouse is currently hovering over this box.
	 * 
	 * @param x The x coordinate of the upper left corner of the box.
	 * @param y The y coordinate of the upper left corner of the box.
	 * @param width the width of the box.
	 * @param height the height of the box.
	 */
	void drawHoveredBox(int x, int y, int width, int height);

	/**
	 * Draws a box. The mouse is currently clicking this box.
	 * 
	 * @param x The x coordinate of the upper left corner of the box.
	 * @param y The y coordinate of the upper left corner of the box.
	 * @param width the width of the box.
	 * @param height the height of the box.
	 */
	void drawClickedBox(int x, int y, int width, int height);

	/**
	 * Draws text.
	 * 
	 * @param text the text.
	 * @param x the x coordinate of the lower left corner of the text.
	 * @param y the y coordinate of the lower left corner of the text.
	 */
	void drawText(String text, int x, int y);

	/**
	 * Draws the label of a button.
	 * 
	 * @param text the text.
	 * @param x the x coordinate of the lower left corner of the text.
	 * @param y the y coordinate of the lower left corner of the text.
	 */
	void drawButtonText(String text, int x, int y);

	/**
	 * Returns the height of text rendered by this renderer.
	 * @return the height of text.
	 */
	int getTextHeight();
}