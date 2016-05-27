package de.isibboi.agentsim.game.entities;

import de.isibboi.agentsim.ui.renderer.Renderer;
import de.isibboi.agentsim.ui.renderer.Visitable;

/**
 * Classes implementing this can be drawn on the screen.
 * @author Sebastian Schmidt
 * @since 0.0.0
 */
public interface Drawable extends Visitable<Drawable, Renderer> {
}
