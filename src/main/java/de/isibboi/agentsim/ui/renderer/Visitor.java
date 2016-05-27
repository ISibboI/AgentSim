package de.isibboi.agentsim.ui.renderer;

/**
 * A visitor.
 * 
 * @author Sebastian Schmidt
 * @since 0.3.0
 * 
 * @param <T> The most general type this visitor can visit.
 */
public interface Visitor<T> {
	/**
	 * Visits the given object.
	 * @param object The object.
	 */
	void visit(Visitable<T, ? extends Visitor<T>> object);
}
