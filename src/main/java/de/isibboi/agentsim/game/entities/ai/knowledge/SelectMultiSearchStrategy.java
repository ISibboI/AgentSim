package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.List;
import java.util.SortedSet;

import de.isibboi.agentsim.algorithm.TemporalVariant;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Entry;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.InnerNode;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Leaf;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Node;

/**
 * Selects the elements with the given indices from this tree.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type of the searched tree.
 */
public class SelectMultiSearchStrategy<T extends Categorized & Prioritized & TemporalVariant> implements MultiSearchStrategy<T> {
	private final SortedSet<Integer> _indices;
	private int _offset = 0;

	/**
	 * Creates a new select multi search strategy selecting the elements at the given indices.
	 * @param indices The element indices.
	 */
	public SelectMultiSearchStrategy(final SortedSet<Integer> indices) {
		_indices = indices;
	}

	@Override
	public boolean condition(final Node<T> node) {
		SortedSet<Integer> subSet = _indices.subSet(_offset, _offset + node.size());

		if (subSet.isEmpty()) {
			_offset += node.size();
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void handleInnerNode(final InnerNode<T> node) {
	}

	@Override
	public List<Entry<T>> handleLeaf(final Leaf<T> node, final List<Entry<T>> result) {
		List<Entry<T>> added = node.select(_indices.subSet(_offset, _offset + node.size()), _offset, result);
		_offset += node.size();
		return added;
	}
}