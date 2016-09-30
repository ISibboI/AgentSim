package de.isibboi.agentsim.ui.renderer;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import de.isibboi.agentsim.game.entities.Drawable;
import de.isibboi.agentsim.game.entities.MapEntity;
import de.isibboi.agentsim.game.map.GameMap;

/**
 * A visitor that renders the content of the GameGLPanel.
 * 
 * @author Sebastian Schmidt
 * @since 0.2.0
 */
public interface Renderer extends Visitor<Drawable> {
	/**
	 * Overload for MapEntities.
	 * @param mapEntity The object to render.
	 */
	void visit(MapEntity mapEntity);

	/**
	 * Overload for GameMap.
	 * @param gameMap The object to render.
	 */
	void visit(GameMap gameMap);

	/**
	 * Sets the current tick.
	 * Used for animation, and thus uses subtick detail.
	 * @param tick The current tick.
	 * @param transition The time partition of the next tick that went by until now.
	 */
	void setCurrentTick(int tick, float transition);

	/**
	 * Starts rendering the image.
	 * Sets the GL object used for rendering.
	 * Should only be called after all other parameters have been set.
	 * @param gl The GL object used for rendering.
	 */
	void startRender(GL3 gl);

	/**
	 * Sets the uniform location of the color variable.
	 * @param colorUL The uniform location of the color variable.
	 */
	void setColorUL(int colorUL);

	/**
	 * Sets the uniform location of the translation variable.
	 * @param translationUL The uniform location of the translation variable.
	 */
	void setTranslationUL(int translationUL);

	/**
	 * Sets the uniform location of the scaling variable.
	 * @param translationUL The uniform location of the scaling variable.
	 */
	void setScalingUL(int scalingUL);

	/**
	 * Sets the size of the element array for a quad.
	 * @param elementSize The size of the element array for a quad.
	 */
	void setElementSize(int elementSize);

	/**
	 * Sets the white texture.
	 * @param whiteTexture A white pixel.
	 */
	void setWhiteTexture(Texture whiteTexture);
}