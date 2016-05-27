package de.isibboi.agentsim.ui.renderer;

/**
 * A type that supports visitors.
 * All subtypes have to override the {@link #getVisited(Visitor)} method for the visitor pattern to work.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * 
 * @param <T> The most general type of this visitable hierarchy.
 * @param <V> The type of the visitor.
 */
public interface Visitable<T, V extends Visitor<T>> {
	/**
	 * Calls the {@link Visitor#visit(Visitable)} method of the given visitor.
	 * @param visitor The visitor that visits this object.
	 */
	void accept(V visitor);
}