package de.isibboi.agentsim.game.entities.ai.knowledge;

import java.util.Objects;

/**
 * A category an object may belong to.
 * A category has a name and an id, and always belongs to a category group.
 * The name can be any String.
 * The id is a number given by the {@link CategoryGroup}, used as an index for {@link CategorySet} data structures.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public final class Category {
	/**
	 * Builds a new category.
	 * 
	 * @author Sebastian Schmidt
	 * @since 0.3.0
	 */
	public static class Builder {
		private String _name;

		/**
		 * Creates a new category builder with the given category name.
		 * 
		 * @param name The name of the category to be built.
		 */
		public Builder(final String name) {
			_name = name;
		}

		/**
		 * Creates a new category.
		 * 
		 * @param id The id of the category.
		 * @param categoryGroup The group the category belongs to.
		 * @return A new category.
		 */
		Category build(final int id, final CategoryGroup categoryGroup) {
			Objects.requireNonNull(_name);
			Objects.requireNonNull(categoryGroup);
			return new Category(_name, id, categoryGroup);
		}

		@Override
		public boolean equals(final Object o) {
			if (o instanceof Builder) {
				Builder b = (Builder) o;

				if (_name == null || b._name == null) {
					return _name == b._name;
				}

				return b._name.equals(_name);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			if (_name == null) {
				return 0;
			} else {
				return _name.hashCode();
			}
		}
	}

	private final String _name;
	private final int _id;
	private final CategoryGroup _categoryGroup;

	/**
	 * Creates a new {@link Category} with the given name and id.
	 * @param name The name of the category.
	 * @param id The id of the category.
	 * @param categoryGroup The {@link CategoryGroup} this category belongs to.
	 */
	private Category(final String name, final int id, final CategoryGroup categoryGroup) {
		_name = name;
		_id = id;
		_categoryGroup = categoryGroup;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof Category) {
			Category c = (Category) o;
			return c._name.equals(_name) && c._categoryGroup.equals(_categoryGroup);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return _name.hashCode() ^ _id ^ _categoryGroup.hashCode();
	}
}