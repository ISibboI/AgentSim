package de.isibboi.agentsim.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.ui.renderer.Renderer;
import de.isibboi.agentsim.ui.renderer.Visitable;
import de.isibboi.agentsim.ui.renderer.Visitor;

/**
 * Renders the default look of the game.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class DefaultRenderer implements Renderer {
	private static final Logger LOG = LogManager.getLogger(DefaultRenderer.class);

	private int _tick;
	private float _transition;

	@Override
	public void setCurrentTick(int tick, float transition) {
		_tick = tick;
		_transition = transition;
	}

	@Override
	public void visit(Visitable<Drawable, ? extends Visitor<Drawable>> object) {
		LOG.warn("Cannot render object: " + object);
	}
}