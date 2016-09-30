package de.isibboi.agentsim.ui.renderer;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.MapEntity;
import de.isibboi.agentsim.game.map.GameMap;
import de.isibboi.agentsim.game.map.Material;
import de.isibboi.agentsim.game.map.Point;

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

	// OpenGL
	protected GL3 _gl;
	protected int _elementSize;

	protected int _colorUL;
	protected int _translationUL;
	protected int _scalingUL;

	protected Texture _whiteTexture;

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

		_gl.glUniform3f(_colorUL, 0.3f, 0.8f, 0.3f);
		_gl.glUniform2f(_translationUL, mapEntity.getLocation().getX(), mapEntity.getLocation().getY());
		_gl.glUniform2f(_scalingUL, 1.0f, 1.0f);

		_gl.glDrawElements(GL3.GL_TRIANGLE_STRIP, _elementSize, GL3.GL_UNSIGNED_SHORT, 0);
	}

	@Override
	public void visit(final GameMap gameMap) {
		for (int x = 0; x < gameMap.getWidth(); x++) {
			for (int y = 0; y < gameMap.getHeight(); y++) {
				Material material = gameMap.getMaterialAt(new Point(x, y));
				Color color = new Color(material.getColor());

				_gl.glUniform3f(_colorUL, (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255);
				_gl.glUniform2f(_translationUL, x, y);
				_gl.glUniform2f(_scalingUL, 1.0f, 1.0f);

				_gl.glDrawElements(GL3.GL_TRIANGLE_STRIP, _elementSize, GL3.GL_UNSIGNED_SHORT, 0);
			}
		}
	}

	@Override
	public void startRender(final GL3 gl) {
		_gl = gl;
		_whiteTexture.bind(gl);
	}

	@Override
	public void setColorUL(final int colorUL) {
		_colorUL = colorUL;
	}

	@Override
	public void setTranslationUL(final int translationUL) {
		_translationUL = translationUL;
	}

	@Override
	public void setScalingUL(final int scalingUL) {
		_scalingUL = scalingUL;
	}

	@Override
	public void setElementSize(final int elementSize) {
		_elementSize = elementSize;
	}

	@Override
	public void setWhiteTexture(Texture whiteTexture) {
		_whiteTexture = whiteTexture;
	}
}