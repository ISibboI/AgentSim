package de.isibboi.agentsim.algorithm;

import java.util.Arrays;
import java.util.Collection;

/**
 * A group of categories that backs a {@link CategorySet}.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public class CategoryGroup {
	private final Category[] _categories;

	/**
	 * Creates a new category group with the given categories.
	 * @param categories The categories.
	 */
	public CategoryGroup(final Collection<Category> categories) {
		_categories = new Category[categories.size()];

		int i = 0;
		for (Category category : categories) {
			_categories[i++] = category;
		}
	}

	/**
	 * Creates a new category group with the given categories.
	 * @param categories The categories.
	 */
	public CategoryGroup(final Category[] categories) {
		_categories = Arrays.copyOf(categories, categories.length);
	}
}