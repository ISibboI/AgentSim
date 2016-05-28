package de.isibboi.agentsim.ui.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.MapEntity;

/**
 * Renders the game using only colored quads.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public class QuadRenderer implements Renderer {
	private static final Logger LOG = LogManager.getLogger(QuadRenderer.class);

	private int _tick;
	private float _transition;

	@Override
	public void setCurrentTick(final int tick, final float transition) {
		_tick = tick;
		_transition = transition;
	}

	@Override
	public void visit(final Visitable<Drawable, ? extends Visitor<Drawable>> object) {
		LOG.warn("Cannot render object: " + object);
	}

	/**
	 * Renders a MapEntity.
	 * @param mapEntity The MapEntity to render.
	 */
	public void visit(final MapEntity mapEntity) {
		LOG.trace("Rendering MapEntity: " + mapEntity);
	}
}