package de.isibboi.agentsim.algorithm;

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
}