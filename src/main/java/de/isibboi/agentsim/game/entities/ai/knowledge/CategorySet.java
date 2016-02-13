package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.Set;

/**
 * A set of categories this object belongs to.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface CategorySet extends Set<Category> {
	/**
	 * Returns the {@link CategoryGroup} that backs this {@code CategorySet}.
	 * @return The {@link CategoryGroup} that backs this {@code CategorySet}.
	 */
	CategoryGroup getCategoryGroup();

	/**
	 * Checks if this category set is a super set of the given one. 
	 * @param other The other category set.
	 * @return True if this is a super set of other.
	 */
	boolean isSuperSetOf(CategorySet other);
}