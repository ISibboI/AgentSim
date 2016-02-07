package de.isibboi.agentsim.algorithm;

/**
 * A type that belongs to certain categories multiple times.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface MultiCategorized extends Categorized {
	/**
	 * Returns the multiset of categories this object belongs to.
	 * @return The multiset of categories this object belongs to.
	 */
	CategoryMultiset getCategorySet();
}