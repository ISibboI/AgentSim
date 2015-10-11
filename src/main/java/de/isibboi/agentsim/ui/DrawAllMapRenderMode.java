package de.isibboi.agentsim.ui;

import java.awt.Graphics2D;

import de.isibboi.agentsim.game.entities.Entities;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * Draws the complete map and all entities.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class DrawAllMapRenderMode implements MapRenderMode {
	@Override
	public void drawMap(final Graphics2D g, final double transition, final GameMap map) {
		map.draw(g, transition);
	}

	@Override
	public void drawEntities(final Graphics2D g, final double transition, final Entities entities) {
		entities.draw(g, transition);
	}
}