package de.isibboi.agentsim.ui.renderer;

import de.isibboi.agentsim.game.entities.Drawable;

/**
 * A visitor that renders the content of the GameGLPanel.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Renderer extends Visitor<Drawable> {
	/**
	 * Sets the current tick.
	 * Used for animation, and thus uses subtick detail.
	 * @param tick The current tick.
	 * @param transition The time partition of the next tick that went by until now.
	 */
	void setCurrentTick(int tick, float transition);
}