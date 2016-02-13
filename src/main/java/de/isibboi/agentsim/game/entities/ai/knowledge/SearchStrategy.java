package de.isibboi.agentsim.game.entities.ai.knowledge;

import de.isibboi.agentsim.algorithm.TemporalVariant;

/**
 * A visitor that searches the {@link MapKnowledgeTree}.
 * It visits exactly one node per layer.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type of the tree.
 */
public interface SearchStrategy<T extends Categorized & Prioritized & TemporalVariant> {
	/**
	 * Checks if the given node is valid for searching.
	 * If this method returns true, the search will enter the node and no other subnodes of the current node will be checked.
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
	 * @return The result of the search operation on the leaf.
	 */
	MapKnowledgeTree.Entry<T> handleLeaf(MapKnowledgeTree.Leaf<T> node);
}
