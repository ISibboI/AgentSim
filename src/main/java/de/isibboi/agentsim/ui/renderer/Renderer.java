package de.isibboi.agentsim.ui.renderer;

import com.jogamp.opengl.GL3;

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
	 * Sets the GL object used for rendering.
	 * @param gl The GL object used for rendering.
	 */
	void setGL(GL3 gl);

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
	 * Sets the size of the element array for a quad.
	 * @param elementSize The size of the element array for a quad.
	 */
	void setElementSize(int elementSize);
}