package de.isibboi.agentsim.ui.component;

import de.isibboi.agentsim.ui.Renderer;


/**
 * An abstract ui component that handles the renderer.
 * @author sibbo
 *
 */
public abstract class UIAbstractComponent implements UIComponent {
	private final Renderer _renderer;

	/**
	 * Creates a new abstract ui component with the given renderer.
	 * @param renderer The renderer used to render this component.
	 */
	public UIAbstractComponent(final Renderer renderer) {
		_renderer = renderer;
	}
	
	/**
	 * Returns the renderer used to render this component.
	 * @return The renderer of this component.
	 */
	public Renderer getRenderer() {
		return _renderer;
	}
}