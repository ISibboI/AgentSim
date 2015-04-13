package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.map.Point;

/**
 * Provides knowledge about a certain feature of the map.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <Knowledge> The type of provider knowledge.
 */
public interface KnowledgeProvider<Knowledge> {
	/**
	 * Returns the current version of the knowledge.
	 * @return The version of the knowledge.
	 */
	int getAge();

	/**
	 * Returns the knowledge about the specified location.
	 * @param location The location.
	 * @return The knowledge about {@code location}.
	 */
	Knowledge getKnowledge(Point location);
}
