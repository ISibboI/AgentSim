package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.List;

import de.isibboi.agentsim.algorithm.TemporalVariant;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Entry;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.InnerNode;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Leaf;
import de.isibboi.agentsim.game.entities.ai.knowledge.MapKnowledgeTree.Node;

/**
 * Returns all entries with the given category.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * @param <T> The element type of the searched tree.
 */
public class CategoryMultiSearchStrategy<T extends Categorized & Prioritized & TemporalVariant> implements MultiSearchStrategy<T> {
	private final CategorySet _categorySet;

	/**
	 * Creates a new multi search strategy searching for all objects with the given categories.
	 * @param categorySet The categories the objects need to fit into.
	 */
	public CategoryMultiSearchStrategy(final CategorySet categorySet) {
		_categorySet = categorySet;
	}

	@Override
	public boolean condition(final Node<T> node) {
		return node.getCategorySet().isSuperSetOf(_categorySet);
	}

	@Override
	public void handleInnerNode(final InnerNode<T> node) {
	}

	@Override
	public List<Entry<T>> handleLeaf(final Leaf<T> node, final List<Entry<T>> result) {
		return node.select(_categorySet, result);
	}
}