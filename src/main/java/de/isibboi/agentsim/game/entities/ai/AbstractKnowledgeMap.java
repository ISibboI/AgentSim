package de.isibboi.agentsim.game.entities.ai;

import java.util.Set;

import de.isibboi.agentsim.game.map.Point;

/**
 * Implements the algorithm for information exchange, nothing more.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <Knowledge> The type of knowledge.
 */
public abstract class AbstractKnowledgeMap<Knowledge> implements KnowledgeMap<Knowledge> {
	@Override
	public void exchangeInformation(final KnowledgeMap<Knowledge> other) {
		Set<Point> knownLocations = getKnownLocationSet();
		knownLocations.addAll(other.getKnownLocationSet());

		exchangeInformation(other, knownLocations);
	}

	/**
	 * Exchanges information about the given locations.
	 * 
	 * @param other The knowledge map to merge with.
	 * @param locations The locations to merge.
	 */
	private void exchangeInformation(final KnowledgeMap<Knowledge> other, final Set<Point> locations) {
		for (Point location : locations) {
			exchangeInformation(other, location);
		}
	}

	/**
	 * Exchanges information about the given location.
	 * 
	 * @param other The knowledge map to merge with.
	 * @param location The location to merge.
	 */
	private void exchangeInformation(final KnowledgeMap<Knowledge> other, final Point location) {
		final long otherAge = other.getLocationKnowledgeAge(location);
		final long thisAge = getLocationKnowledgeAge(location);

		if (otherAge > thisAge) {
			updateLocation(location, other.getLocationKnowledge(location), otherAge);
		} else {
			other.updateLocation(location, getLocationKnowledge(location), thisAge);
		}
	}
}