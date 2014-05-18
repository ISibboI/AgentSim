package de.isibboi.agentsim.ui.component;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.map.Point;

/**
 * A component of the game ui.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface UIComponent extends Drawable {
	/**
	 * Checks if this component contains the given point.
	 * @param point the point.
	 * @return true if this component contains the given point.
	 */
	boolean contains(Point point);

}