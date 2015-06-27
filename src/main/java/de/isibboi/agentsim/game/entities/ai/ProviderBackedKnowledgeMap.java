package de.isibboi.agentsim.game.entities.ai;

import java.util.Set;

import de.isibboi.agentsim.game.map.Point;

/**
 * A knowledge map that uses a {@link KnowledgeProvider} for updating.
 * This decorates a specific implementation of {@link KnowledgeMap}
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <Knowledge> The type of knowledge.
 */
public class ProviderBackedKnowledgeMap<Knowledge> implements KnowledgeMap<Knowledge> {
	private final KnowledgeMap<Knowledge> _knowledgeMap;
	private final KnowledgeProvider<Knowledge> _knowledgeProvider;

	/**
	 * Creates a new object with the given {@code KnowledgeProvider}.
	 * @param knowledgeMap The decorated knowledge map.
	 * @param knowledgeProvider The source of knowledge used for updating.
	 */
	public ProviderBackedKnowledgeMap(final KnowledgeMap<Knowledge> knowledgeMap, final KnowledgeProvider<Knowledge> knowledgeProvider) {
		_knowledgeMap = knowledgeMap;
		_knowledgeProvider = knowledgeProvider;
	}

	@Override
	public void updateLocation(final Point location, final Knowledge knowledge, final long tick) {
		_knowledgeMap.updateLocation(location, knowledge, tick);
	}

	/**
	 * Updates a certain pattern of locations starting from the base location.
	 * 
	 * If {@code base} is {@code (2, 4)}, for example, and pattern contains {@code (1, 1)}, then the location {@code (3, 5)} is updated.
	 * 
	 * @param base The location where the pattern should be applied.
	 * @param pattern The pattern to be applied.
	 */
	public void updateLocation(final Point base, final Iterable<Point> pattern) {
		for (Point direction : pattern) {
			Point location = base.add(direction);
			updateLocation(location);
		}
	}

	/**
	 * Updates the given location.
	 * @param location The location.
	 */
	public void updateLocation(final Point location) {
		_knowledgeMap.updateLocation(location, _knowledgeProvider.getKnowledge(location), _knowledgeProvider.getAge());
	}

	@Override
	public void exchangeInformation(final KnowledgeMap<Knowledge> other) {
		_knowledgeMap.exchangeInformation(other);
	}

	@Override
	public long getLocationKnowledgeAge(final Point location) {
		return _knowledgeMap.getLocationKnowledgeAge(location);
	}

	@Override
	public Knowledge getLocationKnowledge(final Point location) {
		return _knowledgeMap.getLocationKnowledge(location);
	}

	@Override
	public Set<Point> getKnownLocationSet() {
		return _knowledgeMap.getKnownLocationSet();
	}

	/**
	 * Returns the {@link KnowledgeMap} that is decorated by this object.
	 * @return The KnowledgeMap.
	 */
	public KnowledgeMap<Knowledge> getDecoratedKnowledgeMap() {
		return _knowledgeMap;
	}
}