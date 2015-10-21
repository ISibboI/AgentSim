package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;

import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * Defines how the {@link GameMap} should be rendered.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface MapRenderMode {
	/**
	 * Draws the game map.
	 * 
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param transition The current sub-update time, a value between zero and one.
	 * @param map The game map.
	 */
	void drawMap(Graphics2D g, double transition, GameMap map);

	/**
	 * Draws the entities.
	 * 
	 * @param g The {@link Graphics2D} object used for drawing.
	 * @param transition The current sub-update time, a value between zero and one.
	 * @param entities The entities.
	 */
	void drawEntities(Graphics2D g, double transition, Entities entities);
}