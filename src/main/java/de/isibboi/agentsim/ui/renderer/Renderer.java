package de.isibboi.agentsim.ui.renderer;

import de.isibboi.agentsim.game.entities.Drawable;

/**
 * A visitor that renders the content of the GameGLPanel
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Renderer extends Visitor<Drawable> {
	void setCurrentTick(int tick, float transition);
}