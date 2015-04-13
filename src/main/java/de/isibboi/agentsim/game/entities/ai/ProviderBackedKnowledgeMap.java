package de.isibboi.agentsim.game.entities.ai;

import de.isibboi.agentsim.game.map.Point;

/**
 * A knowledge map that uses a {@link KnowledgeProvider} for updating.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 *
 * @param <Knowledge> The type of knowledge.
 */
public class ProviderBackedKnowledgeMap<Knowledge> extends KnowledgeMap<Knowledge> {
	private final KnowledgeProvider<Knowledge> _knowledgeProvider;

	/**
	 * Creates a new object with the given {@code KnowledgeProvider}.
	 * @param width The width of the map.
	 * @param height The height of the map.
	 * @param knowledgeProvider The source of knowledge used for updating.
	 */
	public ProviderBackedKnowledgeMap(final int width, final int height, final KnowledgeProvider<Knowledge> knowledgeProvider) {
		super(width, height);

		_knowledgeProvider = knowledgeProvider;
	}

	/**
	 * This operation is disabled, as the {@code knowledge} and {@code tick} parameter are requested from the {@link KnowledgeProvider}.
	 * 
	 * @param location Ignored.
	 * @param knowledge Ignored.
	 * @param tick Ignored.
	 */
	public void updateLocation(final Point location, final Knowledge knowledge, final int tick) {
		throw new UnsupportedOperationException("This operation is disabled by this class.");
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
		updateLocation(location, _knowledgeProvider.getKnowledge(location), _knowledgeProvider.getAge());
	}
}