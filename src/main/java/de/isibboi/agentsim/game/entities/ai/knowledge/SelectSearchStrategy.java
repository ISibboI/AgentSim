package de.isibboi.agentsim.game.entities.ai.knowledge;

import de.isibboi.agentsim.algorithm.TemporalVariant;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Entry;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.InnerNode;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Leaf;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Node;

/**
 * Selects the n-th element of the tree.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type of the knowledge tree.
 */
public class SelectSearchStrategy<T extends Categorized & Prioritized & TemporalVariant> implements SearchStrategy<T> {
	private int _n;

	/**
	 * Creates a new search visitor selecting the n-th element.
	 * @param n The element number.
	 */
	public SelectSearchStrategy(final int n) {
		_n = n;
	}

	@Override
	public boolean condition(final Node<T> node) {
		if (_n - node.size() < 0) {
			return true;
		} else {
			_n -= node.size();
			return false;
		}
	}

	@Override
	public void handleInnerNode(final InnerNode<T> node) {
	}

	@Override
	public Entry<T> handleLeaf(final Leaf<T> node) {
		return node.select(_n);
	}
}