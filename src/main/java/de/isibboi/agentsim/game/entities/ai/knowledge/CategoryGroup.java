package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A group of categories that backs a {@link CategorySet}.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public final class CategoryGroup {
	/**
	 * Builds a new category group.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	public static class Builder {
		private final Collection<Category.Builder> _categories = new ArrayList<>();

		/**
		 * Creates a new empty builder.
		 */
		public Builder() {
		}

		/**
		 * Adds a category with the given name to this category group builder.
		 * @param name The name of the category.
		 * @return True if the category builder changed as a result of the call.
		 */
		public boolean add(final String name) {
			return add(new Category.Builder(name));
		}

		/**
		 * Adds the given category builder to this category group builder.
		 * @param category The category builder.
		 * @return True if the category builder changed as a result of the call.
		 */
		public boolean add(final Category.Builder category) {
			return _categories.add(category);
		}

		/**
		 * Creates a new {@link CategoryGroup} from this builder.
		 * @return A new {@link CategoryGroup}.
		 */
		public CategoryGroup build() {
			return new CategoryGroup(_categories);
		}
	}

	private final Category[] _categories;

	/**
	 * Creates a new category group with the given categories.
	 * @param categories The categories.
	 */
	private CategoryGroup(final Collection<Category.Builder> categories) {
		_categories = new Category[categories.size()];

		int i = 0;
		for (Category.Builder category : categories) {
			_categories[i] = category.build(i, this);
			i++;
		}
	}
}