package de.isibboi.agentsim.ui.renderer;

import java.awt.image.BufferedImage;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import de.isibboi.agentsim.game.map.GameMap;

/**
 * Caches the rendered map as a texture.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class CachingQuadRenderer extends QuadRenderer {
	@Override
	public void visit(final GameMap gameMap) {
		BufferedImage map = gameMap.getMap();
		Texture texture = AWTTextureIO.newTexture(_gl.getGLProfile(), map, false);
		texture.setTexParameteri(_gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
		texture.setTexParameteri(_gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
		texture.setTexParameteri(_gl, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST);

		texture.bind(_gl);

		_gl.glUniform3f(_colorUL, 1.0f, 1.0f, 1.0f);
		_gl.glUniform2f(_translationUL, 0.0f, 0.0f);
		_gl.glUniform2f(_scalingUL, map.getWidth(), map.getHeight());

		_gl.glDrawElements(GL3.GL_TRIANGLE_STRIP, _elementSize, GL3.GL_UNSIGNED_SHORT, 0);

		_whiteTexture.bind(_gl);
	}
}