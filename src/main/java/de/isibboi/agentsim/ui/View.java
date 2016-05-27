package de.isibboi.agentsim.ui;

import javax.swing.JPanel;

import de.isibboi.agentsim.game.entities.Updateable;
import de.isibboi.agentsim.ui.event.UIActionListener;
import de.isibboi.agentsim.ui.event.UIMouseInputListener;

/**
 * A certain view on the game. Every view defines a UI. The views can be exchanged to show different UI elements, like a main menu.
 * @author Sebastian Schmidt
 * @since 0.3.0
 */
public interface View extends UIMouseInputListener, UIActionListener, Updateable {
	/**
	 * Activate the {@link View}.
	 */
	void activate();

	/**
	 * Deactivate the {@link View}.
	 */
	void deactivate();

	/**
	 * Returns the JPanel that contains this view.
	 * @return The JPanel that contains this view.
	 */
	JPanel getJPanel();
}