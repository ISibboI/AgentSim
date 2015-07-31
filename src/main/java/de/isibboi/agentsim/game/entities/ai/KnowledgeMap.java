package de.isibboi.agentsim.game.entities.ai;

import java.util.Set;

import de.isibboi.agentsim.game.map.Point;

/**
 * Represents knowledge about a specific feature on the map as known to a specific entity.
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <Knowledge> The type of knowledge.
 */
public interface KnowledgeMap<Knowledge> {
	/**
	 * Updates the knowledge about the specified location.
	 * If the given knowledge is older than or equally old as the existing one,
	 * nothing happens.
	 * 
	 * @param location The location.
	 * @param knowledge The knowledge.
	 * @param tick The tick this knowledge was created. Must be greater than zero.
	 */
	void updateLocation(final Point location, final Knowledge knowledge, final long tick);

	/**
	 * Merges the information of this and the given {@code KnowledgeMap}.
	 * Both maps will contain the result of the merge.
	 * For every cell, the newer information is selected.
	 * It is undefined what happens when both maps contain different knowledge with the same age.
	 * 
	 * @param other The {@code KnowledgeMap} to merge with.
	 */
	void exchangeInformation(final KnowledgeMap<Knowledge> other);

	/**
	 * Returns the age of the knowledge about the specified location.
	 * If there is no knowledge about the location, 0 is returned.
	 * 
	 * @param location The location.
	 * @return The age of the knowledge about the specified location.
	 */
	long getLocationKnowledgeAge(Point location);

	/**
	 * Returns the knowledge about the specified location.
	 * If there is no knowledge about the location, null is returned.
	 * 
	 * @param location The location.
	 * @return The knowledge about the specified location.
	 */
	Knowledge getLocationKnowledge(Point location);

	/**
	 * Returns a set that contains all locations this object has knowledge about. 
	 * @return A set containing all known locations.
	 */
	Set<Point> getKnownLocationSet();
}
