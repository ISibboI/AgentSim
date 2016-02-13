package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.List;

import de.isibboi.agentsim.algorithm.TemporalVariant;

/**
 * A strategy for searching the {@link MapKnowledgeTree} and returning multiple elements.
 * The search is performed in a depth first manner.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type of the knowledge tree.
 */
public interface MultiSearchStrategy<T extends Categorized & Prioritized & TemporalVariant> {
	/**
	 * Checks if the given node is valid for searching.
	 * If the condition is met, the search will enter the node immediately.
	 * 
	 * @param node The node.
	 * @return True if the search should enter the given node.
	 */
	boolean condition(MapKnowledgeTree.Node<T> node);

	/**
	 * Entering the given inner node.
	 * @param node The entered inner node.
	 */
	void handleInnerNode(MapKnowledgeTree.InnerNode<T> node);

	/**
	 * Entering the given leaf.
	 * @param node The entered leaf.
	 * @param result The result of the search.
	 * @return The result of the search operation on the leaf.
	 */
	List<MapKnowledgeTree.Entry<T>> handleLeaf(MapKnowledgeTree.Leaf<T> node, List<MapKnowledgeTree.Entry<T>> result);
}
