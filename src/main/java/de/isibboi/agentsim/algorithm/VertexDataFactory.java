package de.isibboi.agentsim.algorithm;

import de.isibboi.agentsim.game.map.Point;

/**
 * Creates new vertex data.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <T> The type of the vertex data.
 */
public interface VertexDataFactory<T> {
	/**
	 * Creates a new vertex data object.
	 * 
	 * @param location The location of the vertex the data is inserted into.
	 * @return The new vertex data.
	 */
	T createVertexData(Point location);
}